package com.votesecure.controller;

import com.votesecure.dto.request.CastVoteRequest;
import com.votesecure.dto.response.TurnoutResponse;
import com.votesecure.dto.response.VoteReceiptResponse;
import com.votesecure.model.vote.Candidate;
import com.votesecure.model.vote.Election;
import com.votesecure.repository.CandidateRepository;
import com.votesecure.repository.ElectionRepository;
import com.votesecure.service.VoteService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/vote")

@Tag(name = "Voting", description = "Core voting operations — cast votes, view ballot, check turnout")
public class VoteController {

    private final VoteService voteService;
    private final ElectionRepository electionRepository;
    private final CandidateRepository candidateRepository;

    @GetMapping("/elections")
    @Operation(summary = "Get all elections")
    public ResponseEntity<List<Election>> getElections() {
        return ResponseEntity.ok(electionRepository.findAll());
    }

    @GetMapping("/elections/active")
    @Operation(summary = "Get currently active elections")
    public ResponseEntity<List<Election>> getActiveElections() {
        return ResponseEntity.ok(electionRepository.findByStatus(Election.ElectionStatus.ACTIVE));
    }

    @GetMapping("/ballot/{electionId}/{constituency}")
    @Operation(summary = "Get ballot — list of candidates for a constituency")
    public ResponseEntity<List<Candidate>> getBallot(
            @PathVariable String electionId,
            @PathVariable String constituency) {
        List<Candidate> candidates = candidateRepository.findByElectionIdAndConstituency(electionId, constituency);
        return ResponseEntity.ok(candidates);
    }

    @PostMapping("/cast")
    @Operation(summary = "Cast an encrypted vote — the core operation")
    public ResponseEntity<VoteReceiptResponse> castVote(
            @Valid @RequestBody CastVoteRequest request,
            @RequestHeader(value = "X-Booth-Id", defaultValue = "DEMO-BOOTH") String boothId) {
        VoteReceiptResponse receipt = voteService.castVote(request, boothId);
        return ResponseEntity.ok(receipt);
    }

    @GetMapping("/turnout/{electionId}")
    @Operation(summary = "Get real-time turnout statistics")
    public ResponseEntity<TurnoutResponse> getTurnout(@PathVariable String electionId) {
        return ResponseEntity.ok(voteService.getTurnout(electionId));
    }

    public VoteController(VoteService voteService, ElectionRepository electionRepository, CandidateRepository candidateRepository) {
        this.voteService = voteService;
        this.electionRepository = electionRepository;
        this.candidateRepository = candidateRepository;
    }
}
