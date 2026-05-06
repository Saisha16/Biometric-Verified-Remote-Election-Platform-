package com.votesecure.repository;

import com.votesecure.model.audit.AuditLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, UUID> {
    List<AuditLog> findByEventTypeOrderByTimestampDesc(String eventType);
    List<AuditLog> findTop50ByOrderByTimestampDesc();
}
