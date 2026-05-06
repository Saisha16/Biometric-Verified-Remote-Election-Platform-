package com.votesecure.service;

import com.votesecure.dto.response.ElectionResultResponse;
import com.votesecure.dto.response.ChainVerificationResponse;
import com.votesecure.exception.ElectionNotFoundException;
import com.votesecure.model.audit.AuditLog;
import com.votesecure.model.vote.Candidate;
import com.votesecure.model.vote.Election;
import com.votesecure.model.vote.EncryptedVote;
import com.votesecure.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Results service — handles vote counting and chain verification.
 * 
 * Counting is only allowed when the election status is COUNTING.
 * Votes are decrypted, tallied, and the results are returned.
 * The hash chain is verified to ensure no tampering.
 */
@Service


public class ResultsService {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(ResultsService.class);


    private final VoteRepository voteRepository;
    private final ElectionRepository electionRepository;
    private final CandidateRepository candidateRepository;
    private final VoterRepository voterRepository;
    private final VoteEncryptionService encryptionService;
    private final HashChainService hashChainService;
    private final AuditService auditService;

    /**
     * Start the counting process for an election.
     */
    @Transactional
    public void startCounting(String electionId, String adminId) {
        Election election = electionRepository.findById(electionId)
                .orElseThrow(() -> new RuntimeException("Election not found"));

        election.setStatus(Election.ElectionStatus.COUNTING);
        electionRepository.save(election);

        auditService.log(AuditLog.COUNTING_STARTED, null, adminId,
                "Counting started for election: " + electionId);
    }

    /**
     * Get election results — decrypt all votes and tally.
     */
    public ElectionResultResponse getResults(String electionId) {
        Election election = electionRepository.findById(electionId)
                .orElseThrow(() -> new ElectionNotFoundException("Election not found with ID: " + electionId));

        if (election.getStatus() != Election.ElectionStatus.COUNTING &&
            election.getStatus() != Election.ElectionStatus.COMPLETED) {
            throw new IllegalStateException("Election counting has not started yet. Status: " + election.getStatus());
        }

        // Get all encrypted votes
        List<EncryptedVote> encryptedVotes = voteRepository.findByElectionIdOrderByVoteTimestampAsc(electionId);

        // Decrypt and tally
        Map<String, Long> tally = new HashMap<>();
        for (EncryptedVote vote : encryptedVotes) {
            String candidateId = encryptionService.decrypt(vote.getEncryptedVote());
            tally.merge(candidateId, 1L, Long::sum);
        }

        // Map candidate IDs to names and parties
        List<Candidate> candidates = candidateRepository.findByElectionId(electionId);
        Map<String, Candidate> candidateMap = candidates.stream()
                .collect(Collectors.toMap(Candidate::getCandidateId, c -> c));

        List<ElectionResultResponse.CandidateResult> results = tally.entrySet().stream()
                .map(entry -> {
                    Candidate c = candidateMap.get(entry.getKey());
                    return ElectionResultResponse.CandidateResult.builder()
                            .candidateId(entry.getKey())
                            .candidateName(c != null ? c.getName() : "Unknown")
                            .party(c != null ? c.getParty() : "Unknown")
                            .voteCount(entry.getValue())
                            .build();
                })
                .sorted((a, b) -> Long.compare(b.getVoteCount(), a.getVoteCount()))
                .toList();

        long totalVotes = encryptedVotes.size();
        long totalRegistered = voterRepository.count();

        return ElectionResultResponse.builder()
                .electionId(electionId)
                .electionName(election.getName())
                .totalVotesCounted(totalVotes)
                .totalRegisteredVoters(totalRegistered)
                .results(results)
                .winner(results.isEmpty() ? null : results.get(0))
                .timestamp(LocalDateTime.now())
                .build();
    }

    /**
     * Verify the hash chain integrity — proves no vote was tampered with.
     */
    public ChainVerificationResponse verifyChain(String electionId) {
        List<EncryptedVote> votes = voteRepository.findByElectionIdOrderByVoteTimestampAsc(electionId);

        List<HashChainService.ChainEntry> chainEntries = votes.stream()
                .map(v -> new HashChainService.ChainEntry(
                        v.getVoteId().toString(),
                        v.getEncryptedVote(),
                        v.getVoteHash()))
                .toList();

        HashChainService.ChainVerificationResult result = hashChainService.verifyChain(chainEntries);

        auditService.log(AuditLog.CHAIN_VERIFIED, null, "SYSTEM",
                "Chain verification result: " + result.message());

        return ChainVerificationResponse.builder()
                .electionId(electionId)
                .chainValid(result.valid())
                .totalVotesVerified(result.verifiedCount())
                .message(result.message())
                .verifiedAt(LocalDateTime.now())
                .build();
    }

    public ResultsService(VoteRepository voteRepository, ElectionRepository electionRepository, CandidateRepository candidateRepository, VoterRepository voterRepository, VoteEncryptionService encryptionService, HashChainService hashChainService, AuditService auditService) {
        this.voteRepository = voteRepository;
        this.electionRepository = electionRepository;
        this.candidateRepository = candidateRepository;
        this.voterRepository = voterRepository;
        this.encryptionService = encryptionService;
        this.hashChainService = hashChainService;
        this.auditService = auditService;
    }
}
