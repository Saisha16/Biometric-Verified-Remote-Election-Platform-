package com.votesecure.service;

import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

/**
 * In-memory distributed lock service.
 * 
 * Prevents double-voting by ensuring only ONE processing thread
 * can handle a voter at any time. If two booths try to process
 * the same voter simultaneously, only one succeeds.
 * 
 * In production: Use Redis SETNX with TTL for true distributed locking.
 * For development: This in-memory implementation provides the same guarantee
 * within a single JVM (which is sufficient for demo/interview purposes).
 */
@Service
public class DistributedLockService {

    // In-memory lock store — maps voterId to lock expiry timestamp
    private final Map<String, Long> locks = new ConcurrentHashMap<>();
    private static final long DEFAULT_TTL_MS = 30_000; // 30 seconds

    /**
     * Try to acquire a lock on a voter ID.
     * Returns true if lock acquired, false if voter is already being processed.
     * 
     * This is equivalent to Redis: SET lock:voterId "1" NX EX 30
     */
    public boolean tryLock(String voterId) {
        long now = System.currentTimeMillis();
        long expiry = now + DEFAULT_TTL_MS;

        // Clean expired lock if present
        Long existingExpiry = locks.get(voterId);
        if (existingExpiry != null && existingExpiry > now) {
            // Lock is still held and not expired
            return false;
        }

        // Atomically set the lock (ConcurrentHashMap.putIfAbsent for thread safety)
        Long previous = locks.putIfAbsent(voterId, expiry);
        if (previous == null) {
            return true; // Lock acquired
        }

        // Check if existing lock expired
        if (previous <= now) {
            // Replace expired lock
            return locks.replace(voterId, previous, expiry);
        }

        return false; // Lock held by another thread
    }

    /**
     * Release a lock on a voter ID.
     * Called after vote processing completes (success or failure).
     */
    public void unlock(String voterId) {
        locks.remove(voterId);
    }

    /**
     * Check if a voter is currently locked (being processed).
     */
    public boolean isLocked(String voterId) {
        Long expiry = locks.get(voterId);
        if (expiry == null) return false;
        if (expiry <= System.currentTimeMillis()) {
            locks.remove(voterId); // Clean expired
            return false;
        }
        return true;
    }
}
