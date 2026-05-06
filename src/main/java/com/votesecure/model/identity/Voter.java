package com.votesecure.model.identity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Voter entity — stored in the IDENTITY schema.
 * Contains WHO voted but NEVER what they voted for.
 * This separation is the core security principle of the system.
 */
@Entity
@Table(name = "voters")
 
 

public class Voter {

    @Id
    @Column(name = "voter_id", length = 20)
    private String voterId;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "aadhaar_hash", nullable = false, length = 64, unique = true)
    private String aadhaarHash;

    @Column(name = "fingerprint_hash", nullable = false, length = 64)
    private String fingerprintHash;

    @Column(name = "constituency", nullable = false, length = 100)
    private String constituency;

    @Column(name = "state", length = 100)
    private String state;

    @Column(name = "has_voted", nullable = false)
    
    private Boolean hasVoted = false;

    @Column(name = "voted_at")
    private LocalDateTime votedAt;

    @Column(name = "voting_booth_id", length = 20)
    private String votingBoothId;

    @Column(name = "created_at", nullable = false)
    
    private LocalDateTime createdAt = LocalDateTime.now();

    public Voter() {}

    public Voter(String voterId, String name, String aadhaarHash, String fingerprintHash, String constituency, String state, Boolean hasVoted, LocalDateTime votedAt, String votingBoothId, LocalDateTime createdAt) {
        this.voterId = voterId;
        this.name = name;
        this.aadhaarHash = aadhaarHash;
        this.fingerprintHash = fingerprintHash;
        this.constituency = constituency;
        this.state = state;
        this.hasVoted = hasVoted;
        this.votedAt = votedAt;
        this.votingBoothId = votingBoothId;
        this.createdAt = createdAt;
    }

    public String getVoterId() { return this.voterId; }

    public void setVoterId(String voterId) { this.voterId = voterId; }

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

    public String getAadhaarHash() { return this.aadhaarHash; }

    public void setAadhaarHash(String aadhaarHash) { this.aadhaarHash = aadhaarHash; }

    public String getFingerprintHash() { return this.fingerprintHash; }

    public void setFingerprintHash(String fingerprintHash) { this.fingerprintHash = fingerprintHash; }

    public String getConstituency() { return this.constituency; }

    public void setConstituency(String constituency) { this.constituency = constituency; }

    public String getState() { return this.state; }

    public void setState(String state) { this.state = state; }

    public Boolean getHasVoted() { return this.hasVoted; }

    public void setHasVoted(Boolean hasVoted) { this.hasVoted = hasVoted; }

    public LocalDateTime getVotedAt() { return this.votedAt; }

    public void setVotedAt(LocalDateTime votedAt) { this.votedAt = votedAt; }

    public String getVotingBoothId() { return this.votingBoothId; }

    public void setVotingBoothId(String votingBoothId) { this.votingBoothId = votingBoothId; }

    public LocalDateTime getCreatedAt() { return this.createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static class VoterBuilder {
        private String voterId;
        private String name;
        private String aadhaarHash;
        private String fingerprintHash;
        private String constituency;
        private String state;
        private Boolean hasVoted = false;
        private LocalDateTime votedAt;
        private String votingBoothId;
        private LocalDateTime createdAt = LocalDateTime.now();

        public VoterBuilder voterId(String voterId) {
            this.voterId = voterId;
            return this;
        }

        public VoterBuilder name(String name) {
            this.name = name;
            return this;
        }

        public VoterBuilder aadhaarHash(String aadhaarHash) {
            this.aadhaarHash = aadhaarHash;
            return this;
        }

        public VoterBuilder fingerprintHash(String fingerprintHash) {
            this.fingerprintHash = fingerprintHash;
            return this;
        }

        public VoterBuilder constituency(String constituency) {
            this.constituency = constituency;
            return this;
        }

        public VoterBuilder state(String state) {
            this.state = state;
            return this;
        }

        public VoterBuilder hasVoted(Boolean hasVoted) {
            this.hasVoted = hasVoted;
            return this;
        }

        public VoterBuilder votedAt(LocalDateTime votedAt) {
            this.votedAt = votedAt;
            return this;
        }

        public VoterBuilder votingBoothId(String votingBoothId) {
            this.votingBoothId = votingBoothId;
            return this;
        }

        public VoterBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Voter build() {
            return new Voter(this.voterId, this.name, this.aadhaarHash, this.fingerprintHash, this.constituency, this.state, this.hasVoted, this.votedAt, this.votingBoothId, this.createdAt);
        }
    }

    public static VoterBuilder builder() { return new VoterBuilder(); }
}
