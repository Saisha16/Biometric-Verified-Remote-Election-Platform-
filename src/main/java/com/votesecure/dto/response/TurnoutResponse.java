package com.votesecure.dto.response;

import java.time.LocalDateTime;

 
 

public class TurnoutResponse {
    private String electionId;
    private long totalRegisteredVoters;
    private long totalVotesCast;
    private long totalVoteRecords;
    private double turnoutPercentage;
    private boolean countsMatch; // Identity count == Vote count (integrity check)
    private LocalDateTime timestamp;

    public TurnoutResponse() {}

    public TurnoutResponse(String electionId, long totalRegisteredVoters, long totalVotesCast, long totalVoteRecords, double turnoutPercentage, boolean countsMatch, LocalDateTime timestamp) {
        this.electionId = electionId;
        this.totalRegisteredVoters = totalRegisteredVoters;
        this.totalVotesCast = totalVotesCast;
        this.totalVoteRecords = totalVoteRecords;
        this.turnoutPercentage = turnoutPercentage;
        this.countsMatch = countsMatch;
        this.timestamp = timestamp;
    }

    public String getElectionId() { return this.electionId; }

    public void setElectionId(String electionId) { this.electionId = electionId; }

    public long getTotalRegisteredVoters() { return this.totalRegisteredVoters; }

    public void setTotalRegisteredVoters(long totalRegisteredVoters) { this.totalRegisteredVoters = totalRegisteredVoters; }

    public long getTotalVotesCast() { return this.totalVotesCast; }

    public void setTotalVotesCast(long totalVotesCast) { this.totalVotesCast = totalVotesCast; }

    public long getTotalVoteRecords() { return this.totalVoteRecords; }

    public void setTotalVoteRecords(long totalVoteRecords) { this.totalVoteRecords = totalVoteRecords; }

    public double getTurnoutPercentage() { return this.turnoutPercentage; }

    public void setTurnoutPercentage(double turnoutPercentage) { this.turnoutPercentage = turnoutPercentage; }

    public boolean getCountsMatch() { return this.countsMatch; }

    public void setCountsMatch(boolean countsMatch) { this.countsMatch = countsMatch; }

    public LocalDateTime getTimestamp() { return this.timestamp; }

    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public static class TurnoutResponseBuilder {
        private String electionId;
        private long totalRegisteredVoters;
        private long totalVotesCast;
        private long totalVoteRecords;
        private double turnoutPercentage;
        private boolean countsMatch;
        private LocalDateTime timestamp;

        public TurnoutResponseBuilder electionId(String electionId) {
            this.electionId = electionId;
            return this;
        }

        public TurnoutResponseBuilder totalRegisteredVoters(long totalRegisteredVoters) {
            this.totalRegisteredVoters = totalRegisteredVoters;
            return this;
        }

        public TurnoutResponseBuilder totalVotesCast(long totalVotesCast) {
            this.totalVotesCast = totalVotesCast;
            return this;
        }

        public TurnoutResponseBuilder totalVoteRecords(long totalVoteRecords) {
            this.totalVoteRecords = totalVoteRecords;
            return this;
        }

        public TurnoutResponseBuilder turnoutPercentage(double turnoutPercentage) {
            this.turnoutPercentage = turnoutPercentage;
            return this;
        }

        public TurnoutResponseBuilder countsMatch(boolean countsMatch) {
            this.countsMatch = countsMatch;
            return this;
        }

        public TurnoutResponseBuilder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public TurnoutResponse build() {
            return new TurnoutResponse(this.electionId, this.totalRegisteredVoters, this.totalVotesCast, this.totalVoteRecords, this.turnoutPercentage, this.countsMatch, this.timestamp);
        }
    }

    public static TurnoutResponseBuilder builder() { return new TurnoutResponseBuilder(); }
}
