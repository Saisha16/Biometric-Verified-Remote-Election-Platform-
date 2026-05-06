package com.votesecure.dto.response;

import java.time.LocalDateTime;
import java.util.List;

public class ElectionResultResponse {
    private String electionId;
    private String electionName;
    private long totalVotesCounted;
    private long totalRegisteredVoters;
    private List<CandidateResult> results;
    private CandidateResult winner;
    private LocalDateTime timestamp;

    public static class CandidateResult {
        private String candidateId;
        private String candidateName;
        private String party;
        private long voteCount;

        public CandidateResult() {}

        public CandidateResult(String candidateId, String candidateName, String party, long voteCount) {
            this.candidateId = candidateId;
            this.candidateName = candidateName;
            this.party = party;
            this.voteCount = voteCount;
        }

        public String getCandidateId() { return candidateId; }
        public void setCandidateId(String candidateId) { this.candidateId = candidateId; }
        public String getCandidateName() { return candidateName; }
        public void setCandidateName(String candidateName) { this.candidateName = candidateName; }
        public String getParty() { return party; }
        public void setParty(String party) { this.party = party; }
        public long getVoteCount() { return voteCount; }
        public void setVoteCount(long voteCount) { this.voteCount = voteCount; }

        public static CandidateResultBuilder builder() {
            return new CandidateResultBuilder();
        }

        public static class CandidateResultBuilder {
            private String candidateId;
            private String candidateName;
            private String party;
            private long voteCount;

            public CandidateResultBuilder candidateId(String candidateId) {
                this.candidateId = candidateId; return this;
            }
            public CandidateResultBuilder candidateName(String candidateName) {
                this.candidateName = candidateName; return this;
            }
            public CandidateResultBuilder party(String party) {
                this.party = party; return this;
            }
            public CandidateResultBuilder voteCount(long voteCount) {
                this.voteCount = voteCount; return this;
            }
            public CandidateResult build() {
                return new CandidateResult(candidateId, candidateName, party, voteCount);
            }
        }
    }

    public ElectionResultResponse() {}

    public ElectionResultResponse(String electionId, String electionName, long totalVotesCounted, 
                                  long totalRegisteredVoters, List<CandidateResult> results, 
                                  CandidateResult winner, LocalDateTime timestamp) {
        this.electionId = electionId;
        this.electionName = electionName;
        this.totalVotesCounted = totalVotesCounted;
        this.totalRegisteredVoters = totalRegisteredVoters;
        this.results = results;
        this.winner = winner;
        this.timestamp = timestamp;
    }

    public String getElectionId() { return electionId; }
    public void setElectionId(String electionId) { this.electionId = electionId; }
    public String getElectionName() { return electionName; }
    public void setElectionName(String electionName) { this.electionName = electionName; }
    public long getTotalVotesCounted() { return totalVotesCounted; }
    public void setTotalVotesCounted(long totalVotesCounted) { this.totalVotesCounted = totalVotesCounted; }
    public long getTotalRegisteredVoters() { return totalRegisteredVoters; }
    public void setTotalRegisteredVoters(long totalRegisteredVoters) { this.totalRegisteredVoters = totalRegisteredVoters; }
    public List<CandidateResult> getResults() { return results; }
    public void setResults(List<CandidateResult> results) { this.results = results; }
    public CandidateResult getWinner() { return winner; }
    public void setWinner(CandidateResult winner) { this.winner = winner; }
    public LocalDateTime getTimestamp() { return timestamp; }
    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public static ElectionResultResponseBuilder builder() {
        return new ElectionResultResponseBuilder();
    }

    public static class ElectionResultResponseBuilder {
        private String electionId;
        private String electionName;
        private long totalVotesCounted;
        private long totalRegisteredVoters;
        private List<CandidateResult> results;
        private CandidateResult winner;
        private LocalDateTime timestamp;

        public ElectionResultResponseBuilder electionId(String electionId) {
            this.electionId = electionId; return this;
        }
        public ElectionResultResponseBuilder electionName(String electionName) {
            this.electionName = electionName; return this;
        }
        public ElectionResultResponseBuilder totalVotesCounted(long totalVotesCounted) {
            this.totalVotesCounted = totalVotesCounted; return this;
        }
        public ElectionResultResponseBuilder totalRegisteredVoters(long totalRegisteredVoters) {
            this.totalRegisteredVoters = totalRegisteredVoters; return this;
        }
        public ElectionResultResponseBuilder results(List<CandidateResult> results) {
            this.results = results; return this;
        }
        public ElectionResultResponseBuilder winner(CandidateResult winner) {
            this.winner = winner; return this;
        }
        public ElectionResultResponseBuilder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp; return this;
        }
        public ElectionResultResponse build() {
            return new ElectionResultResponse(electionId, electionName, totalVotesCounted, 
                                              totalRegisteredVoters, results, winner, timestamp);
        }
    }
}
