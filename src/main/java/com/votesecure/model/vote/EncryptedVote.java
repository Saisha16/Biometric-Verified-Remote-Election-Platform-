package com.votesecure.model.vote;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Encrypted Vote — stored in the VOTE schema.
 * Contains WHAT was voted but NEVER who voted.
 * 
 * CRITICAL: There is NO voter_id column here. This is BY DESIGN.
 * Even with full database access, you cannot link a vote to a voter.
 */
@Entity
@Table(name = "encrypted_votes")
 
 

public class EncryptedVote {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "vote_id")
    private UUID voteId;

    @Column(name = "encrypted_vote", nullable = false, columnDefinition = "TEXT")
    private String encryptedVote;

    @Column(name = "vote_hash", nullable = false, length = 88)
    private String voteHash;

    @Column(name = "previous_hash", length = 88)
    private String previousHash;

    @Column(name = "vote_timestamp", nullable = false)
    private LocalDateTime voteTimestamp;

    @Column(name = "election_id", nullable = false, length = 20)
    private String electionId;

    @Column(name = "constituency", nullable = false, length = 100)
    private String constituency;

    // NOTE: There is intentionally NO voter_id or booth_id field here.
    // This ensures ballot secrecy at the schema level.

    public EncryptedVote() {}

    public EncryptedVote(UUID voteId, String encryptedVote, String voteHash, String previousHash, LocalDateTime voteTimestamp, String electionId, String constituency) {
        this.voteId = voteId;
        this.encryptedVote = encryptedVote;
        this.voteHash = voteHash;
        this.previousHash = previousHash;
        this.voteTimestamp = voteTimestamp;
        this.electionId = electionId;
        this.constituency = constituency;
    }

    public UUID getVoteId() { return this.voteId; }

    public void setVoteId(UUID voteId) { this.voteId = voteId; }

    public String getEncryptedVote() { return this.encryptedVote; }

    public void setEncryptedVote(String encryptedVote) { this.encryptedVote = encryptedVote; }

    public String getVoteHash() { return this.voteHash; }

    public void setVoteHash(String voteHash) { this.voteHash = voteHash; }

    public String getPreviousHash() { return this.previousHash; }

    public void setPreviousHash(String previousHash) { this.previousHash = previousHash; }

    public LocalDateTime getVoteTimestamp() { return this.voteTimestamp; }

    public void setVoteTimestamp(LocalDateTime voteTimestamp) { this.voteTimestamp = voteTimestamp; }

    public String getElectionId() { return this.electionId; }

    public void setElectionId(String electionId) { this.electionId = electionId; }

    public String getConstituency() { return this.constituency; }

    public void setConstituency(String constituency) { this.constituency = constituency; }

    public static class EncryptedVoteBuilder {
        private UUID voteId;
        private String encryptedVote;
        private String voteHash;
        private String previousHash;
        private LocalDateTime voteTimestamp;
        private String electionId;
        private String constituency;

        public EncryptedVoteBuilder voteId(UUID voteId) {
            this.voteId = voteId;
            return this;
        }

        public EncryptedVoteBuilder encryptedVote(String encryptedVote) {
            this.encryptedVote = encryptedVote;
            return this;
        }

        public EncryptedVoteBuilder voteHash(String voteHash) {
            this.voteHash = voteHash;
            return this;
        }

        public EncryptedVoteBuilder previousHash(String previousHash) {
            this.previousHash = previousHash;
            return this;
        }

        public EncryptedVoteBuilder voteTimestamp(LocalDateTime voteTimestamp) {
            this.voteTimestamp = voteTimestamp;
            return this;
        }

        public EncryptedVoteBuilder electionId(String electionId) {
            this.electionId = electionId;
            return this;
        }

        public EncryptedVoteBuilder constituency(String constituency) {
            this.constituency = constituency;
            return this;
        }

        public EncryptedVote build() {
            return new EncryptedVote(this.voteId, this.encryptedVote, this.voteHash, this.previousHash, this.voteTimestamp, this.electionId, this.constituency);
        }
    }

    public static EncryptedVoteBuilder builder() { return new EncryptedVoteBuilder(); }
}
