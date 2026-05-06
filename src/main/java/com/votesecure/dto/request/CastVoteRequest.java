package com.votesecure.dto.request;

import jakarta.validation.constraints.NotBlank;

 
 

public class CastVoteRequest {

    @NotBlank(message = "Voter ID is required")
    private String voterId;

    @NotBlank(message = "Election ID is required")
    private String electionId;

    @NotBlank(message = "Candidate ID is required")
    private String candidateId;

    public CastVoteRequest() {}

    public CastVoteRequest(String voterId, String electionId, String candidateId) {
        this.voterId = voterId;
        this.electionId = electionId;
        this.candidateId = candidateId;
    }

    public String getVoterId() { return this.voterId; }

    public void setVoterId(String voterId) { this.voterId = voterId; }

    public String getElectionId() { return this.electionId; }

    public void setElectionId(String electionId) { this.electionId = electionId; }

    public String getCandidateId() { return this.candidateId; }

    public void setCandidateId(String candidateId) { this.candidateId = candidateId; }

    public static class CastVoteRequestBuilder {
        private String voterId;
        private String electionId;
        private String candidateId;

        public CastVoteRequestBuilder voterId(String voterId) {
            this.voterId = voterId;
            return this;
        }

        public CastVoteRequestBuilder electionId(String electionId) {
            this.electionId = electionId;
            return this;
        }

        public CastVoteRequestBuilder candidateId(String candidateId) {
            this.candidateId = candidateId;
            return this;
        }

        public CastVoteRequest build() {
            return new CastVoteRequest(this.voterId, this.electionId, this.candidateId);
        }
    }

    public static CastVoteRequestBuilder builder() { return new CastVoteRequestBuilder(); }
}
