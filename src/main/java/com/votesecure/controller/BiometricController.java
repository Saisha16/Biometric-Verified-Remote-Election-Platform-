package com.votesecure.controller;

import com.votesecure.dto.request.BiometricVerifyRequest;
import com.votesecure.model.audit.AuditLog;
import com.votesecure.model.identity.Voter;
import com.votesecure.repository.VoterRepository;
import com.votesecure.service.AadhaarMockService;
import com.votesecure.service.AuditService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/biometric")

@Tag(name = "Biometric Verification", description = "Aadhaar-based voter identity verification")
public class BiometricController {

    private final AadhaarMockService aadhaarService;
    private final VoterRepository voterRepository;
    private final AuditService auditService;

    @PostMapping("/verify")
    @Operation(summary = "Verify voter identity using Aadhaar + fingerprint")
    public ResponseEntity<?> verifyBiometric(@Valid @RequestBody BiometricVerifyRequest request) {
        // Step 1: Verify biometric with mock Aadhaar service
        AadhaarMockService.BiometricResult result = aadhaarService.verify(
                request.getAadhaarNumber(), request.getFingerprintData());

        if (!result.verified()) {
            auditService.log(AuditLog.BIOMETRIC_FAILED, null, "SYSTEM",
                    "Biometric verification failed: " + result.message());
            return ResponseEntity.status(401).body(Map.of(
                    "verified", false,
                    "message", result.message()
            ));
        }

        // Step 2: Find voter by Aadhaar hash
        Voter voter = voterRepository.findByAadhaarHash(result.aadhaarHash())
                .orElse(null);

        if (voter == null) {
            return ResponseEntity.status(404).body(Map.of(
                    "verified", false,
                    "message", "Voter not found in electoral roll"
            ));
        }

        auditService.log(AuditLog.VOTER_VERIFIED, null, "SYSTEM",
                "Voter verified: " + voter.getVoterId());

        return ResponseEntity.ok(Map.of(
                "verified", true,
                "voterId", voter.getVoterId(),
                "name", voter.getName(),
                "constituency", voter.getConstituency(),
                "hasVoted", voter.getHasVoted(),
                "message", "Identity verified successfully"
        ));
    }

    @GetMapping("/voter/{voterId}/status")
    @Operation(summary = "Check if a voter has already voted")
    public ResponseEntity<?> getVoterStatus(@PathVariable String voterId) {
        Voter voter = voterRepository.findById(voterId).orElse(null);
        if (voter == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(Map.of(
                "voterId", voter.getVoterId(),
                "hasVoted", voter.getHasVoted(),
                "votedAt", voter.getVotedAt() != null ? voter.getVotedAt().toString() : "N/A"
        ));
    }

    public BiometricController(AadhaarMockService aadhaarService, VoterRepository voterRepository, AuditService auditService) {
        this.aadhaarService = aadhaarService;
        this.voterRepository = voterRepository;
        this.auditService = auditService;
    }
}
