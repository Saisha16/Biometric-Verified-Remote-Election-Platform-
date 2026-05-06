package com.votesecure.dto.response;

import java.time.LocalDateTime;

 
 

public class ChainVerificationResponse {
    private String electionId;
    private boolean chainValid;
    private int totalVotesVerified;
    private String message;
    private LocalDateTime verifiedAt;

    public ChainVerificationResponse() {}

    public ChainVerificationResponse(String electionId, boolean chainValid, int totalVotesVerified, String message, LocalDateTime verifiedAt) {
        this.electionId = electionId;
        this.chainValid = chainValid;
        this.totalVotesVerified = totalVotesVerified;
        this.message = message;
        this.verifiedAt = verifiedAt;
    }

    public String getElectionId() { return this.electionId; }

    public void setElectionId(String electionId) { this.electionId = electionId; }

    public boolean getChainValid() { return this.chainValid; }

    public void setChainValid(boolean chainValid) { this.chainValid = chainValid; }

    public int getTotalVotesVerified() { return this.totalVotesVerified; }

    public void setTotalVotesVerified(int totalVotesVerified) { this.totalVotesVerified = totalVotesVerified; }

    public String getMessage() { return this.message; }

    public void setMessage(String message) { this.message = message; }

    public LocalDateTime getVerifiedAt() { return this.verifiedAt; }

    public void setVerifiedAt(LocalDateTime verifiedAt) { this.verifiedAt = verifiedAt; }

    public static class ChainVerificationResponseBuilder {
        private String electionId;
        private boolean chainValid;
        private int totalVotesVerified;
        private String message;
        private LocalDateTime verifiedAt;

        public ChainVerificationResponseBuilder electionId(String electionId) {
            this.electionId = electionId;
            return this;
        }

        public ChainVerificationResponseBuilder chainValid(boolean chainValid) {
            this.chainValid = chainValid;
            return this;
        }

        public ChainVerificationResponseBuilder totalVotesVerified(int totalVotesVerified) {
            this.totalVotesVerified = totalVotesVerified;
            return this;
        }

        public ChainVerificationResponseBuilder message(String message) {
            this.message = message;
            return this;
        }

        public ChainVerificationResponseBuilder verifiedAt(LocalDateTime verifiedAt) {
            this.verifiedAt = verifiedAt;
            return this;
        }

        public ChainVerificationResponse build() {
            return new ChainVerificationResponse(this.electionId, this.chainValid, this.totalVotesVerified, this.message, this.verifiedAt);
        }
    }

    public static ChainVerificationResponseBuilder builder() { return new ChainVerificationResponseBuilder(); }
}
