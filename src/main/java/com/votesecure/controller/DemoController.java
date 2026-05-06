package com.votesecure.controller;

import com.votesecure.dto.request.CastVoteRequest;
import com.votesecure.dto.response.VoteReceiptResponse;
import com.votesecure.service.VoteService;
import com.votesecure.service.ResultsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * Demo controller — allows testing the full voting flow without authentication.
 * This is for demo/interview purposes only.
 */
@RestController
@RequestMapping("/api/demo")

@Tag(name = "Demo", description = "Test the voting flow without authentication (demo only)")
public class DemoController {

    private final VoteService voteService;
    private final ResultsService resultsService;

    @PostMapping("/vote")
    @Operation(summary = "Cast a vote without auth (demo mode)")
    public ResponseEntity<VoteReceiptResponse> demoVote(@RequestBody CastVoteRequest request) {
        return ResponseEntity.ok(voteService.castVote(request, "DEMO-BOOTH-001"));
    }

    @PostMapping("/start-counting/{electionId}")
    @Operation(summary = "Start counting (demo mode)")
    public ResponseEntity<?> demoCounting(@PathVariable String electionId) {
        resultsService.startCounting(electionId, "demo-admin");
        return ResponseEntity.ok(Map.of("message", "Counting started", "electionId", electionId));
    }

    @GetMapping("/results/{electionId}")
    @Operation(summary = "Get results (demo mode)")
    public ResponseEntity<?> demoResults(@PathVariable String electionId) {
        return ResponseEntity.ok(resultsService.getResults(electionId));
    }

    @GetMapping("/verify-chain/{electionId}")
    @Operation(summary = "Verify hash chain integrity (demo mode)")
    public ResponseEntity<?> demoVerifyChain(@PathVariable String electionId) {
        return ResponseEntity.ok(resultsService.verifyChain(electionId));
    }

    public DemoController(VoteService voteService, ResultsService resultsService) {
        this.voteService = voteService;
        this.resultsService = resultsService;
    }
}
