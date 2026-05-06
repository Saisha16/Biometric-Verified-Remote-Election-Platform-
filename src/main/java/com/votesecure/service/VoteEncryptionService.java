package com.votesecure.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * AES-256/GCM encryption service for votes.
 * 
 * Each vote is encrypted with a random IV (Initialization Vector),
 * so two identical votes produce different ciphertext.
 * This prevents pattern analysis on encrypted data.
 * 
 * The encryption key is stored separately from the data (environment variable).
 * In production, this would come from an HSM (Hardware Security Module).
 */
@Service
public class VoteEncryptionService {

    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int GCM_TAG_LENGTH = 128;
    private static final int IV_LENGTH = 12;

    private final SecretKeySpec keySpec;
    private final SecureRandom secureRandom = new SecureRandom();

    public VoteEncryptionService(@Value("${election.encryption.key}") String base64Key) {
        byte[] keyBytes = Base64.getDecoder().decode(base64Key);
        // Ensure key is 16 bytes (128-bit) minimum for AES
        byte[] validKey = new byte[16];
        System.arraycopy(keyBytes, 0, validKey, 0, Math.min(keyBytes.length, 16));
        this.keySpec = new SecretKeySpec(validKey, ALGORITHM);
    }

    /**
     * Encrypt a candidate ID using AES-256/GCM with a random IV.
     * Returns: Base64(IV):Base64(ciphertext)
     */
    public String encrypt(String candidateId) {
        try {
            // Generate random IV — makes each encryption unique
            byte[] iv = new byte[IV_LENGTH];
            secureRandom.nextBytes(iv);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, gcmSpec);

            byte[] encrypted = cipher.doFinal(candidateId.getBytes(StandardCharsets.UTF_8));

            // Return IV:ciphertext — IV is needed for decryption
            String ivBase64 = Base64.getEncoder().encodeToString(iv);
            String cipherBase64 = Base64.getEncoder().encodeToString(encrypted);
            return ivBase64 + ":" + cipherBase64;

        } catch (Exception e) {
            throw new RuntimeException("Vote encryption failed", e);
        }
    }

    /**
     * Decrypt an encrypted vote back to the candidate ID.
     * Only used during official counting phase.
     */
    public String decrypt(String encryptedVote) {
        try {
            String[] parts = encryptedVote.split(":");
            if (parts.length != 2) {
                throw new IllegalArgumentException("Invalid encrypted vote format");
            }

            byte[] iv = Base64.getDecoder().decode(parts[0]);
            byte[] ciphertext = Base64.getDecoder().decode(parts[1]);

            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            GCMParameterSpec gcmSpec = new GCMParameterSpec(GCM_TAG_LENGTH, iv);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmSpec);

            byte[] decrypted = cipher.doFinal(ciphertext);
            return new String(decrypted, StandardCharsets.UTF_8);

        } catch (Exception e) {
            throw new RuntimeException("Vote decryption failed", e);
        }
    }
}
