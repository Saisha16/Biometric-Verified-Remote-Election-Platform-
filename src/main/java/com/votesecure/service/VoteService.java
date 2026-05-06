package com.votesecure.service;

import com.votesecure.dto.request.CastVoteRequest;
import com.votesecure.dto.response.VoteReceiptResponse;
import com.votesecure.dto.response.TurnoutResponse;
import com.votesecure.exception.*;
import com.votesecure.model.audit.AuditLog;
import com.votesecure.model.identity.Voter;
import com.votesecure.model.vote.Election;
import com.votesecure.model.vote.EncryptedVote;
import com.votesecure.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * Core voting service — orchestrates the entire vote casting process.
 * 
 * The flow:
 * 1. Acquire distributed lock on voter ID (prevent simultaneous voting)
 * 2. Verify voter hasn't already voted
 * 3. Encrypt the vote (AES-256/GCM)
 * 4. Compute hash chain (SHA-256)
 * 5. Store encrypted vote (NO voter identity attached)
 * 6. Mark voter as HAS_VOTED in identity DB
 * 7. Log audit event
 * 8. Release lock
 * 9. Return receipt
 */
@Service


public class VoteService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(VoteService.class);


    private final VoterRepository voterRepository;
    private final VoteRepository voteRepository;
    private final ElectionRepository electionRepository;
    private final CandidateRepository candidateRepository;
    private final VoteEncryptionService encryptionService;
    private final HashChainService hashChainService;
    private final DistributedLockService lockService;
    private final AuditService auditService;

    /**
     * Cast a vote — the most critical operation in the system.
     */
    @Transactional
    public VoteReceiptResponse castVote(CastVoteRequest request, String boothId) {
        String voterId = request.getVoterId();

        // Step 1: Acquire distributed lock
        if (!lockService.tryLock(voterId)) {
            throw new VotingInProgressException(
                "This voter is currently being processed at another terminal. Please wait."
            );
        }

        try {
            // Step 2: Validate election is active
            Election election = electionRepository.findById(request.getElectionId())
                    .orElseThrow(() -> new ElectionNotFoundException("Election not found: " + request.getElectionId()));

            if (!election.isActive()) {
                throw new ElectionClosedException("Election '" + election.getName() + "' is not currently active. Status: " + election.getStatus());
            }

            // Step 3: Verify voter exists and hasn't voted
            Voter voter = voterRepository.findById(voterId)
                    .orElseThrow(() -> new VoterNotFoundException("Voter not found: " + voterId));

            if (voter.getHasVoted()) {
                auditService.log(AuditLog.DOUBLE_VOTE_ATTEMPT, boothId, voterId,
                        "Double vote attempt detected. Already voted at booth: " + voter.getVotingBoothId());
                throw new AlreadyVotedException(
                    "This voter has already cast their vote on " + voter.getVotedAt() + " at booth " + voter.getVotingBoothId()
                );
            }

            // Step 4: Validate candidate exists
            boolean validCandidate = candidateRepository.findById(request.getCandidateId()).isPresent();
            if (!validCandidate) {
                throw new InvalidCandidateException("Invalid candidate: " + request.getCandidateId());
            }

            // Step 5: Encrypt the vote
            String encryptedVote = encryptionService.encrypt(request.getCandidateId());

            // Step 6: Compute hash chain
            String previousHash = getLastHash(request.getElectionId());
            String voteHash = hashChainService.computeHash(encryptedVote, previousHash);

            // Step 7: Store encrypted vote — NO voter identity attached
            EncryptedVote vote = EncryptedVote.builder()
                    .encryptedVote(encryptedVote)
                    .voteHash(voteHash)
                    .previousHash(previousHash)
                    .voteTimestamp(LocalDateTime.now())
                    .electionId(request.getElectionId())
                    .constituency(voter.getConstituency())
                    .build();
            voteRepository.save(vote);

            // Step 8: Mark voter as voted in identity DB
            voter.setHasVoted(true);
            voter.setVotedAt(LocalDateTime.now());
            voter.setVotingBoothId(boothId);
            voterRepository.save(voter);

            // Step 9: Audit log (NO candidate info — preserves secrecy)
            auditService.log(AuditLog.VOTE_CAST, boothId, "SYSTEM",
                    "Vote cast successfully for election: " + request.getElectionId());

            // Step 10: Generate receipt
            String receiptCode = generateReceiptCode();
            log.info("Vote cast successfully. Receipt: {}", receiptCode);

            return VoteReceiptResponse.builder()
                    .receiptCode(receiptCode)
                    .message("Your vote has been recorded securely and anonymously.")
                    .electionId(request.getElectionId())
                    .timestamp(LocalDateTime.now())
                    .build();

        } finally {
            // Always release lock — even if exception occurs
            lockService.unlock(voterId);
        }
    }

    /**
     * Get the last vote hash in the chain for an election.
     */
    private String getLastHash(String electionId) {
        List<EncryptedVote> votes = voteRepository.findByElectionIdOrderByVoteTimestampAsc(electionId);
        if (votes.isEmpty()) {
            return HashChainService.GENESIS_HASH;
        }
        return votes.get(votes.size() - 1).getVoteHash();
    }

    /**
     * Generate a unique receipt code for the voter.
     */
    private String generateReceiptCode() {
        return "VS-" + UUID.randomUUID().toString().substring(0, 8).toUpperCase();
    }

    /**
     * Get real-time turnout statistics.
     */
    public TurnoutResponse getTurnout(String electionId) {
        long totalVoters = voterRepository.count();
        long votedCount = voterRepository.countByHasVotedTrue();
        long voteRecords = voteRepository.countByElectionId(electionId);
        double percentage = totalVoters > 0 ? (double) votedCount / totalVoters * 100 : 0;

        return TurnoutResponse.builder()
                .electionId(electionId)
                .totalRegisteredVoters(totalVoters)
                .totalVotesCast(votedCount)
                .totalVoteRecords(voteRecords)
                .turnoutPercentage(Math.round(percentage * 100.0) / 100.0)
                .countsMatch(votedCount == voteRecords)
                .timestamp(LocalDateTime.now())
                .build();
    }

    public VoteService(VoterRepository voterRepository, VoteRepository voteRepository, ElectionRepository electionRepository, CandidateRepository candidateRepository, VoteEncryptionService encryptionService, HashChainService hashChainService, DistributedLockService lockService, AuditService auditService) {
        this.voterRepository = voterRepository;
        this.voteRepository = voteRepository;
        this.electionRepository = electionRepository;
        this.candidateRepository = candidateRepository;
        this.encryptionService = encryptionService;
        this.hashChainService = hashChainService;
        this.lockService = lockService;
        this.auditService = auditService;
    }
}
