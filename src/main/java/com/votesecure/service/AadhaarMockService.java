package com.votesecure.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Mock Aadhaar Biometric Verification Service.
 * 
 * Simulates UIDAI's biometric authentication API.
 * In production, this would integrate with the real UIDAI system.
 * The interface is designed to match the real API contract, so
 * swapping in the real service is a configuration change, not code change.
 * 
 * This demonstrates the Dependency Inversion Principle —
 * coding to interfaces, not implementations.
 */
@Service
public class AadhaarMockService {

    @Value("${election.aadhaar.salt}")
    private String aadhaarSalt;

    // In-memory store of enrolled mock biometrics (development only)
    private final Map<String, String> enrolledFingerprints = new ConcurrentHashMap<>();

    /**
     * Hash an Aadhaar number using SHA-256 with salt.
     * The raw Aadhaar number is NEVER stored anywhere.
     */
    public String hashAadhaar(String aadhaarNumber) {
        return sha256(aadhaarNumber + aadhaarSalt);
    }

    /**
     * Hash a fingerprint template.
     * In production, this would process actual biometric data from a scanner.
     */
    public String hashFingerprint(String fingerprintData) {
        return sha256(fingerprintData + aadhaarSalt);
    }

    /**
     * Enroll a mock voter's biometric data (for testing/demo).
     */
    public void enrollMockBiometric(String aadhaarNumber, String fingerprintData) {
        String aadhaarHash = hashAadhaar(aadhaarNumber);
        String fingerprintHash = hashFingerprint(fingerprintData);
        enrolledFingerprints.put(aadhaarHash, fingerprintHash);
    }

    /**
     * Verify biometric — checks if the fingerprint matches the Aadhaar.
     * Returns a verification result with status and message.
     */
    public BiometricResult verify(String aadhaarNumber, String fingerprintData) {
        String aadhaarHash = hashAadhaar(aadhaarNumber);
        String fingerprintHash = hashFingerprint(fingerprintData);

        // Check if this biometric is enrolled
        String storedFingerprint = enrolledFingerprints.get(aadhaarHash);

        if (storedFingerprint == null) {
            return new BiometricResult(false, aadhaarHash, "Aadhaar not found in biometric database");
        }

        if (!storedFingerprint.equals(fingerprintHash)) {
            return new BiometricResult(false, aadhaarHash, "Fingerprint does not match");
        }

        return new BiometricResult(true, aadhaarHash, "Biometric verification successful");
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
     * Result of biometric verification.
     */
    public record BiometricResult(boolean verified, String aadhaarHash, String message) {}
}
