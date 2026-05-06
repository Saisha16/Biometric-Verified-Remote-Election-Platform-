package com.votesecure.model.audit;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * Immutable audit log entry.
 * This table is append-only — no updates or deletes allowed.
 * Records every significant system event for accountability.
 */
@Entity
@Table(name = "audit_log")
 
 

public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "log_id")
    private UUID logId;

    @Column(name = "event_type", nullable = false, length = 50)
    private String eventType;

    @Column(name = "booth_id", length = 20)
    private String boothId;

    @Column(name = "performed_by", length = 100)
    private String performedBy;

    @Column(name = "details", columnDefinition = "TEXT")
    private String details;

    @Column(name = "timestamp", nullable = false)
    
    private LocalDateTime timestamp = LocalDateTime.now();

    // Common event types
    public static final String VOTER_VERIFIED = "VOTER_VERIFIED";
    public static final String VOTE_CAST = "VOTE_CAST";
    public static final String BIOMETRIC_FAILED = "BIOMETRIC_FAILED";
    public static final String DOUBLE_VOTE_ATTEMPT = "DOUBLE_VOTE_ATTEMPT";
    public static final String COUNTING_STARTED = "COUNTING_STARTED";
    public static final String CHAIN_VERIFIED = "CHAIN_VERIFIED";
    public static final String RESULTS_PUBLISHED = "RESULTS_PUBLISHED";
    public static final String LOGIN = "LOGIN";

    public AuditLog() {}

    public AuditLog(UUID logId, String eventType, String boothId, String performedBy, String details, LocalDateTime timestamp) {
        this.logId = logId;
        this.eventType = eventType;
        this.boothId = boothId;
        this.performedBy = performedBy;
        this.details = details;
        this.timestamp = timestamp;
    }

    public UUID getLogId() { return this.logId; }

    public void setLogId(UUID logId) { this.logId = logId; }

    public String getEventType() { return this.eventType; }

    public void setEventType(String eventType) { this.eventType = eventType; }

    public String getBoothId() { return this.boothId; }

    public void setBoothId(String boothId) { this.boothId = boothId; }

    public String getPerformedBy() { return this.performedBy; }

    public void setPerformedBy(String performedBy) { this.performedBy = performedBy; }

    public String getDetails() { return this.details; }

    public void setDetails(String details) { this.details = details; }

    public LocalDateTime getTimestamp() { return this.timestamp; }

    public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }

    public static class AuditLogBuilder {
        private UUID logId;
        private String eventType;
        private String boothId;
        private String performedBy;
        private String details;
        private LocalDateTime timestamp = LocalDateTime.now();

        public AuditLogBuilder logId(UUID logId) {
            this.logId = logId;
            return this;
        }

        public AuditLogBuilder eventType(String eventType) {
            this.eventType = eventType;
            return this;
        }

        public AuditLogBuilder boothId(String boothId) {
            this.boothId = boothId;
            return this;
        }

        public AuditLogBuilder performedBy(String performedBy) {
            this.performedBy = performedBy;
            return this;
        }

        public AuditLogBuilder details(String details) {
            this.details = details;
            return this;
        }

        public AuditLogBuilder timestamp(LocalDateTime timestamp) {
            this.timestamp = timestamp;
            return this;
        }

        public AuditLog build() {
            return new AuditLog(this.logId, this.eventType, this.boothId, this.performedBy, this.details, this.timestamp);
        }
    }

    public static AuditLogBuilder builder() { return new AuditLogBuilder(); }
}
