package com.votesecure.service;

import com.votesecure.model.audit.AuditLog;
import com.votesecure.repository.AuditLogRepository;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Audit service — writes immutable log entries for every significant action.
 * The audit table is append-only — no updates or deletes allowed.
 */
@Service

public class AuditService {

    private final AuditLogRepository auditLogRepository;

    public void log(String eventType, String boothId, String performedBy, String details) {
        AuditLog entry = AuditLog.builder()
                .eventType(eventType)
                .boothId(boothId)
                .performedBy(performedBy)
                .details(details)
                .build();
        auditLogRepository.save(entry);
    }

    public List<AuditLog> getRecentLogs() {
        return auditLogRepository.findTop50ByOrderByTimestampDesc();
    }

    public List<AuditLog> getLogsByType(String eventType) {
        return auditLogRepository.findByEventTypeOrderByTimestampDesc(eventType);
    }

    public AuditService(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }
}
