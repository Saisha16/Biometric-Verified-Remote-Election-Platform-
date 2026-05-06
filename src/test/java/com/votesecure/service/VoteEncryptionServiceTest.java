package com.votesecure.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for AES-256/GCM vote encryption.
 * Verifies:
 * - Encrypt/decrypt roundtrip works
 * - Same input produces different ciphertext (random IV)
 * - Decryption with wrong data fails
 */
class VoteEncryptionServiceTest {

    private VoteEncryptionService encryptionService;

    @BeforeEach
    void setUp() {
        // Base64-encoded 16-byte key for testing
        String testKey = "dGhpc0lzQVNlY3JldEtleUZvclZvdGVFbmNyeXB0aW9u";
        encryptionService = new VoteEncryptionService(testKey);
    }

    @Test
    @DisplayName("Encrypt and decrypt should return original candidate ID")
    void testEncryptDecryptRoundtrip() {
        String candidateId = "C001";
        String encrypted = encryptionService.encrypt(candidateId);
        String decrypted = encryptionService.decrypt(encrypted);
        assertEquals(candidateId, decrypted);
    }

    @Test
    @DisplayName("Same candidate should produce different ciphertext each time (random IV)")
    void testRandomIVProducesDifferentCiphertext() {
        String candidateId = "C001";
        String encrypted1 = encryptionService.encrypt(candidateId);
        String encrypted2 = encryptionService.encrypt(candidateId);

        // Must be different — random IV ensures this
        assertNotEquals(encrypted1, encrypted2,
                "Two encryptions of the same vote must produce different ciphertext");

        // But both must decrypt to the same value
        assertEquals(candidateId, encryptionService.decrypt(encrypted1));
        assertEquals(candidateId, encryptionService.decrypt(encrypted2));
    }

    @Test
    @DisplayName("Encrypted vote should not contain the candidate ID in plaintext")
    void testCiphertextDoesNotContainPlaintext() {
        String candidateId = "C001";
        String encrypted = encryptionService.encrypt(candidateId);
        assertFalse(encrypted.contains(candidateId),
                "Encrypted vote must not contain candidate ID in plaintext");
    }

    @Test
    @DisplayName("Decryption with tampered ciphertext should fail")
    void testTamperedCiphertextFails() {
        String encrypted = encryptionService.encrypt("C001");
        String tampered = encrypted.substring(0, encrypted.length() - 2) + "XX";
        assertThrows(RuntimeException.class, () -> encryptionService.decrypt(tampered));
    }
}
