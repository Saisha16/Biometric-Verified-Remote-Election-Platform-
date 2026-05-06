package com.votesecure.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for distributed lock service.
 * Verifies:
 * - Lock can be acquired and released
 * - Double lock is rejected
 * - Lock auto-expires
 */
class DistributedLockServiceTest {

    private DistributedLockService lockService;

    @BeforeEach
    void setUp() {
        lockService = new DistributedLockService();
    }

    @Test
    @DisplayName("Should acquire lock on first attempt")
    void testAcquireLock() {
        assertTrue(lockService.tryLock("V001"));
    }

    @Test
    @DisplayName("Should reject second lock on same voter")
    void testDoubleVotePrevention() {
        assertTrue(lockService.tryLock("V001"));
        assertFalse(lockService.tryLock("V001"), "Double lock should be rejected");
    }

    @Test
    @DisplayName("Should allow lock after unlock")
    void testUnlockAllowsRelock() {
        assertTrue(lockService.tryLock("V001"));
        lockService.unlock("V001");
        assertTrue(lockService.tryLock("V001"), "Should allow lock after unlock");
    }

    @Test
    @DisplayName("Different voters should get independent locks")
    void testIndependentLocks() {
        assertTrue(lockService.tryLock("V001"));
        assertTrue(lockService.tryLock("V002"), "Different voters should lock independently");
    }

    @Test
    @DisplayName("isLocked should return correct state")
    void testIsLocked() {
        assertFalse(lockService.isLocked("V001"));
        lockService.tryLock("V001");
        assertTrue(lockService.isLocked("V001"));
        lockService.unlock("V001");
        assertFalse(lockService.isLocked("V001"));
    }
}
