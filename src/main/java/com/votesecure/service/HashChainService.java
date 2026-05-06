package com.votesecure.service;

import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;

/**
 * SHA-256 Hash Chain service for tamper detection.
 * 
 * Each vote's hash includes the hash of the previous vote,
 * creating a chain. If ANY vote is modified, every subsequent
 * hash becomes invalid — making tampering detectable.
 * 
 * This is the same principle as blockchain, without the
 * decentralization overhead (ECI is the single trusted authority).
 */
@Service
public class HashChainService {

    public static final String GENESIS_HASH = "GENESIS_VOTESECURE_2024";

    /**
     * Compute hash for a new vote, chaining it to the previous hash.
     */
    public String computeHash(String encryptedVote, String previousHash) {
        String input = encryptedVote + "|" + previousHash;
        return sha256(input);
    }

    /**
     * Verify the entire hash chain for an election.
     * Returns true if ALL hashes are valid (no tampering).
     * Returns false if ANY hash is broken (tampering detected).
     */
    public ChainVerificationResult verifyChain(List<ChainEntry> votes) {
        if (votes.isEmpty()) {
            return new ChainVerificationResult(true, 0, "No votes to verify");
        }

        String expectedPreviousHash = GENESIS_HASH;
        int verifiedCount = 0;

        for (ChainEntry vote : votes) {
            String computedHash = computeHash(vote.encryptedVote(), expectedPreviousHash);

            if (!computedHash.equals(vote.voteHash())) {
                return new ChainVerificationResult(
                    false,
                    verifiedCount,
                    String.format("Chain broken at vote #%d (ID: %s). Expected hash: %s, Found: %s",
                        verifiedCount + 1, vote.voteId(), computedHash, vote.voteHash())
                );
            }

            expectedPreviousHash = vote.voteHash();
            verifiedCount++;
        }

        return new ChainVerificationResult(
            true, verifiedCount,
            String.format("All %d votes verified. Chain integrity confirmed.", verifiedCount)
        );
    }

    private String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hashBytes);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    /**
     * Record representing a vote entry for chain verification.
     */
    public record ChainEntry(String voteId, String encryptedVote, String voteHash) {}

    /**
     * Result of hash chain verification.
     */
    public record ChainVerificationResult(boolean valid, int verifiedCount, String message) {}
}
