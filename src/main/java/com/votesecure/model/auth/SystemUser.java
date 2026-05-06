package com.votesecure.model.auth;

import jakarta.persistence.*;
import java.time.LocalDateTime;

/**
 * System user — booth officers and election admins.
 * Voters authenticate via Aadhaar biometric, not username/password.
 */
@Entity
@Table(name = "system_users")
 
 

public class SystemUser {

    @Id
    @Column(name = "user_id", length = 50)
    private String userId;

    @Column(name = "username", nullable = false, unique = true, length = 50)
    private String username;

    @Column(name = "password_hash", nullable = false)
    private String passwordHash;

    @Column(name = "full_name", nullable = false, length = 200)
    private String fullName;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, length = 20)
    private UserRole role;

    @Column(name = "booth_id", length = 20)
    private String boothId;

    @Column(name = "is_active", nullable = false)
    
    private Boolean isActive = true;

    @Column(name = "created_at", nullable = false)
    
    private LocalDateTime createdAt = LocalDateTime.now();

    public enum UserRole {
        BOOTH_OFFICER, ELECTION_ADMIN, COUNTING_TEAM
    }

    public SystemUser() {}

    public SystemUser(String userId, String username, String passwordHash, String fullName, UserRole role, String boothId, Boolean isActive, LocalDateTime createdAt) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.role = role;
        this.boothId = boothId;
        this.isActive = isActive;
        this.createdAt = createdAt;
    }

    public String getUserId() { return this.userId; }

    public void setUserId(String userId) { this.userId = userId; }

    public String getUsername() { return this.username; }

    public void setUsername(String username) { this.username = username; }

    public String getPasswordHash() { return this.passwordHash; }

    public void setPasswordHash(String passwordHash) { this.passwordHash = passwordHash; }

    public String getFullName() { return this.fullName; }

    public void setFullName(String fullName) { this.fullName = fullName; }

    public UserRole getRole() { return this.role; }

    public void setRole(UserRole role) { this.role = role; }

    public String getBoothId() { return this.boothId; }

    public void setBoothId(String boothId) { this.boothId = boothId; }

    public Boolean getIsActive() { return this.isActive; }

    public void setIsActive(Boolean isActive) { this.isActive = isActive; }

    public LocalDateTime getCreatedAt() { return this.createdAt; }

    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }

    public static class SystemUserBuilder {
        private String userId;
        private String username;
        private String passwordHash;
        private String fullName;
        private UserRole role;
        private String boothId;
        private Boolean isActive = true;
        private LocalDateTime createdAt = LocalDateTime.now();

        public SystemUserBuilder userId(String userId) {
            this.userId = userId;
            return this;
        }

        public SystemUserBuilder username(String username) {
            this.username = username;
            return this;
        }

        public SystemUserBuilder passwordHash(String passwordHash) {
            this.passwordHash = passwordHash;
            return this;
        }

        public SystemUserBuilder fullName(String fullName) {
            this.fullName = fullName;
            return this;
        }

        public SystemUserBuilder role(UserRole role) {
            this.role = role;
            return this;
        }

        public SystemUserBuilder boothId(String boothId) {
            this.boothId = boothId;
            return this;
        }

        public SystemUserBuilder isActive(Boolean isActive) {
            this.isActive = isActive;
            return this;
        }

        public SystemUserBuilder createdAt(LocalDateTime createdAt) {
            this.createdAt = createdAt;
            return this;
        }

        public SystemUser build() {
            return new SystemUser(this.userId, this.username, this.passwordHash, this.fullName, this.role, this.boothId, this.isActive, this.createdAt);
        }
    }

    public static SystemUserBuilder builder() { return new SystemUserBuilder(); }
}
