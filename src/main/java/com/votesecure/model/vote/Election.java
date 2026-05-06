package com.votesecure.model.vote;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * Election configuration — defines an election event with start/end times.
 */
@Entity
@Table(name = "elections")
 
 

public class Election {

    @Id
    @Column(name = "election_id", length = 20)
    private String electionId;

    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "start_time", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "end_time", nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    
    private ElectionStatus status = ElectionStatus.UPCOMING;

    @Column(name = "created_at", nullable = false)
    
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum ElectionStatus {
        UPCOMING, ACTIVE, COUNTING, COMPLETED
    }

    public boolean isActive() {
        return status == ElectionStatus.ACTIVE;
    }

    public Election() {}

    public Election(String electionId, String name, LocalDateTime startTime, LocalDateTime endTime, ElectionStatus status, LocalDateTime createdAt) {
        this.electionId = electionId;
        this.name = name;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
        this.createdAt = createdAt;
    }

    public String getElectionId() { return this.electionId; }

    public void setElectionId(String electionId) { this.electionId = electionId; }

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

    public LocalDateTime getStartTime() { return this.startTime; }

    public void setStartTime(LocalDateTime startTime) { this.startTime = startTime; }

    public LocalDateTime getEndTime() { return this.endTime; }

    public void setEndTime(LocalDateTime endTime) { this.endTime = endTime; }

    public ElectionStatus getStatus() { return this.status; }

    public void setStatus(ElectionStatus status) { this.status = status; }

    public LocalDateTime getCreatedAt() { return this.createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static class ElectionBuilder {
        private String electionId;
        private String name;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private ElectionStatus status = ElectionStatus.UPCOMING;
        private LocalDateTime createdAt = LocalDateTime.now();

        public ElectionBuilder electionId(String electionId) {
            this.electionId = electionId;
            return this;
        }

        public ElectionBuilder name(String name) {
            this.name = name;
            return this;
        }

        public ElectionBuilder startTime(LocalDateTime startTime) {
            this.startTime = startTime;
            return this;
        }

        public ElectionBuilder endTime(LocalDateTime endTime) {
            this.endTime = endTime;
            return this;
        }

        public ElectionBuilder status(ElectionStatus status) {
            this.status = status;
            return this;
        }

        public ElectionBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public Election build() {
            return new Election(this.electionId, this.name, this.startTime, this.endTime, this.status, this.createdAt);
        }
    }

    public static ElectionBuilder builder() { return new ElectionBuilder(); }
}
