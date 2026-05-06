package com.votesecure.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for SHA-256 hash chain integrity.
 * Verifies:
 * - Valid chain passes verification
 * - Tampered vote breaks the chain
 * - Empty chain is valid
 */
class HashChainServiceTest {

    private HashChainService hashChainService;

    @BeforeEach
    void setUp() {
        hashChainService = new HashChainService();
    }

    @Test
    @DisplayName("Valid chain should pass verification")
    void testValidChain() {
        // Build a chain of 3 votes
        String prev = HashChainService.GENESIS_HASH;

        String vote1 = "encrypted_vote_1";
        String hash1 = hashChainService.computeHash(vote1, prev);

        String vote2 = "encrypted_vote_2";
        String hash2 = hashChainService.computeHash(vote2, hash1);

        String vote3 = "encrypted_vote_3";
        String hash3 = hashChainService.computeHash(vote3, hash2);

        List<HashChainService.ChainEntry> chain = List.of(
                new HashChainService.ChainEntry("1", vote1, hash1),
                new HashChainService.ChainEntry("2", vote2, hash2),
                new HashChainService.ChainEntry("3", vote3, hash3)
        );

        HashChainService.ChainVerificationResult result = hashChainService.verifyChain(chain);

        assertTrue(result.valid(), "Valid chain should pass verification");
        assertEquals(3, result.verifiedCount());
    }

    @Test
    @DisplayName("Tampered vote should break the chain")
    void testTamperedChainFails() {
        String prev = HashChainService.GENESIS_HASH;

        String vote1 = "encrypted_vote_1";
        String hash1 = hashChainService.computeHash(vote1, prev);

        String vote2 = "encrypted_vote_2";
        String hash2 = hashChainService.computeHash(vote2, hash1);

        // Tamper vote2's encrypted content but keep the old hash
        List<HashChainService.ChainEntry> chain = List.of(
                new HashChainService.ChainEntry("1", vote1, hash1),
                new HashChainService.ChainEntry("2", "TAMPERED_VOTE", hash2) // Tampered!
        );

        HashChainService.ChainVerificationResult result = hashChainService.verifyChain(chain);

        assertFalse(result.valid(), "Tampered chain should fail verification");
        assertEquals(1, result.verifiedCount(), "Should detect break at second vote");
    }

    @Test
    @DisplayName("Empty chain should be valid")
    void testEmptyChain() {
        HashChainService.ChainVerificationResult result = hashChainService.verifyChain(List.of());
        assertTrue(result.valid());
    }

    @Test
    @DisplayName("Same input should always produce same hash (deterministic)")
    void testDeterministicHash() {
        String hash1 = hashChainService.computeHash("vote", "prev");
        String hash2 = hashChainService.computeHash("vote", "prev");
        assertEquals(hash1, hash2, "Hash function must be deterministic");
    }

    @Test
    @DisplayName("Different inputs should produce different hashes")
    void testDifferentInputsDifferentHashes() {
        String hash1 = hashChainService.computeHash("vote_A", "prev");
        String hash2 = hashChainService.computeHash("vote_B", "prev");
        assertNotEquals(hash1, hash2);
    }
}
