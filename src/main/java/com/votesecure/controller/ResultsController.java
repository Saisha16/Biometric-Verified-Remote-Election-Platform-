package com.votesecure.controller;

import com.votesecure.dto.response.ChainVerificationResponse;
import com.votesecure.dto.response.ElectionResultResponse;
import com.votesecure.service.ResultsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/results")

@Tag(name = "Results", description = "Election results and integrity verification (Admin only)")
public class ResultsController {

    private final ResultsService resultsService;

    @PostMapping("/start-counting/{electionId}")
    @Operation(summary = "Start counting process for an election")
    public ResponseEntity<?> startCounting(
            @PathVariable String electionId,
            @RequestHeader(value = "X-Admin-Id", defaultValue = "admin") String adminId) {
        resultsService.startCounting(electionId, adminId);
        return ResponseEntity.ok().body(java.util.Map.of(
                "message", "Counting started for election: " + electionId,
                "status", "COUNTING"
        ));
    }

    @GetMapping("/{electionId}")
    @Operation(summary = "Get election results — decrypts and tallies all votes")
    public ResponseEntity<ElectionResultResponse> getResults(@PathVariable String electionId) {
        return ResponseEntity.ok(resultsService.getResults(electionId));
    }

    @GetMapping("/{electionId}/verify-chain")
    @Operation(summary = "Verify hash chain integrity — proves no vote was tampered")
    public ResponseEntity<ChainVerificationResponse> verifyChain(@PathVariable String electionId) {
        return ResponseEntity.ok(resultsService.verifyChain(electionId));
    }

    public ResultsController(ResultsService resultsService) {
        this.resultsService = resultsService;
    }
}
