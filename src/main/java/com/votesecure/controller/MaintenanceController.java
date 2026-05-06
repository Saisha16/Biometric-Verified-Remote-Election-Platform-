package com.votesecure.controller;

import com.votesecure.repository.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.votesecure.config.DataSeeder;

import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@Tag(name = "Maintenance", description = "System maintenance and reset utilities")
public class MaintenanceController {

    private final VoteRepository voteRepository;
    private final VoterRepository voterRepository;
    private final ElectionRepository electionRepository;

    public MaintenanceController(VoteRepository voteRepository, VoterRepository voterRepository, 
                                 ElectionRepository electionRepository) {
        this.voteRepository = voteRepository;
        this.voterRepository = voterRepository;
        this.electionRepository = electionRepository;
    }

    @PostMapping("/reset-election")
    @Operation(summary = "Reset all votes and restore election to ACTIVE state")
    public ResponseEntity<?> resetElection() {
        // Clear all votes
        voteRepository.deleteAll();
        
        // Reset all voters 'hasVoted' status
        voterRepository.findAll().forEach(voter -> {
            voter.setHasVoted(false);
            voter.setVotedAt(null);
            voter.setVotingBoothId(null);
            voterRepository.save(voter);
        });
        
        // Restore election status to ACTIVE
        electionRepository.findAll().forEach(election -> {
            election.setStatus(com.votesecure.model.vote.Election.ElectionStatus.ACTIVE);
            electionRepository.save(election);
        });

        return ResponseEntity.ok(Map.of(
            "message", "Election data has been reset successfully. System is ready for a new round of voting.",
            "status", "ACTIVE"
        ));
    }
}
