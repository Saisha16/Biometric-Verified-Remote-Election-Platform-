package com.votesecure.config;

import com.votesecure.model.auth.SystemUser;
import com.votesecure.model.identity.Booth;
import com.votesecure.model.identity.Voter;
import com.votesecure.model.vote.Candidate;
import com.votesecure.model.vote.Election;
import com.votesecure.model.vote.EncryptedVote;
import com.votesecure.repository.*;
import com.votesecure.service.AadhaarMockService;
import com.votesecure.service.HashChainService;
import com.votesecure.service.VoteEncryptionService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Seeds demo data on startup for testing and interview demonstrations.
 * Creates sample election, candidates, voters, and system users.
 */
@Component


public class DataSeeder implements CommandLineRunner {
    private static final org.slf4j.Logger log = org.slf4j.LoggerFactory.getLogger(DataSeeder.class);


    private final ElectionRepository electionRepo;
    private final CandidateRepository candidateRepo;
    private final VoterRepository voterRepo;
    private final VoteRepository voteRepo;
    private final BoothRepository boothRepo;
    private final SystemUserRepository userRepo;
    private final AadhaarMockService aadhaarService;
    private final PasswordEncoder passwordEncoder;
    private final VoteEncryptionService encryptionService;
    private final HashChainService hashChainService;

    @org.springframework.beans.factory.annotation.Value("${server.port:8080}")
    private String port;

    @Override
    public void run(String... args) {
        log.info("=== Seeding Demo Data ===");

        // Create Election
        Election election = Election.builder()
                .electionId("GE2024")
                .name("General Election 2024 - Lok Sabha")
                .startTime(LocalDateTime.now().minusHours(1))
                .endTime(LocalDateTime.now().plusHours(12))
                .status(Election.ElectionStatus.ACTIVE)
                .build();
        electionRepo.save(election);

        // Create Booths
        boothRepo.save(Booth.builder().boothId("MUM-B001").location("Mumbai Central School").district("Mumbai").state("Maharashtra").build());
        boothRepo.save(Booth.builder().boothId("DEL-B001").location("Delhi Public Library").district("New Delhi").state("Delhi").build());
        boothRepo.save(Booth.builder().boothId("BLR-B001").location("Bangalore Town Hall").district("Bangalore").state("Karnataka").build());

        // Create Candidates — Mumbai North constituency
        candidateRepo.save(Candidate.builder().candidateId("C001").name("Rajesh Kumar").party("Party A").partySymbol("🌸").constituency("Mumbai-North").electionId("GE2024").build());
        candidateRepo.save(Candidate.builder().candidateId("C002").name("Priya Sharma").party("Party B").partySymbol("🌿").constituency("Mumbai-North").electionId("GE2024").build());
        candidateRepo.save(Candidate.builder().candidateId("C003").name("Amit Patel").party("Party C").partySymbol("⭐").constituency("Mumbai-North").electionId("GE2024").build());
        candidateRepo.save(Candidate.builder().candidateId("C004").name("NOTA").party("None of the Above").partySymbol("❌").constituency("Mumbai-North").electionId("GE2024").build());

        // Create Voters with mock Aadhaar biometrics
        createVoter("V001", "Arjun Mehta", "1234-5678-9012", "fingerprint_arjun", "Mumbai-North", "Maharashtra");
        createVoter("V002", "Sneha Reddy", "2345-6789-0123", "fingerprint_sneha", "Mumbai-North", "Maharashtra");
        createVoter("V003", "Vikram Singh", "3456-7890-1234", "fingerprint_vikram", "Mumbai-North", "Maharashtra");
        createVoter("V004", "Ananya Iyer", "4567-8901-2345", "fingerprint_ananya", "Mumbai-North", "Maharashtra");
        createVoter("V005", "Rahul Gupta", "5678-9012-3456", "fingerprint_rahul", "Mumbai-North", "Maharashtra");
        createVoter("V006", "Kavya Menon", "6789-0123-4567", "fingerprint_kavya", "Mumbai-North", "Maharashtra");
        createVoter("V007", "Rohan Das", "7890-1234-5678", "fingerprint_rohan", "Mumbai-North", "Maharashtra");
        createVoter("V008", "Neha Kapoor", "8901-2345-6789", "fingerprint_neha", "Mumbai-North", "Maharashtra");
        createVoter("V009", "Siddharth Bose", "9012-3456-7890", "fingerprint_siddharth", "Mumbai-North", "Maharashtra");
        createVoter("V010", "Ayesha Khan", "0123-4567-8901", "fingerprint_ayesha", "Mumbai-North", "Maharashtra");
        createVoter("V011", "Ishan Malhotra", "1111-2222-3333", "fingerprint_ishan", "Mumbai-North", "Maharashtra");
        createVoter("V012", "Tanvi Jain", "4444-5555-6666", "fingerprint_tanvi", "Mumbai-North", "Maharashtra");
        createVoter("V013", "Aditya Verma", "7777-8888-9999", "fingerprint_aditya", "Mumbai-North", "Maharashtra");
        createVoter("V014", "Meera Nair", "1212-3434-5656", "fingerprint_meera", "Mumbai-North", "Maharashtra");
        createVoter("V015", "Kabir Bansal", "7878-9090-1212", "fingerprint_kabir", "Mumbai-North", "Maharashtra");

        // Create System Users (Booth Officers + Admin)
        userRepo.save(SystemUser.builder()
                .userId("U001").username("officer1").passwordHash(passwordEncoder.encode("officer123"))
                .fullName("Booth Officer - Mumbai").role(SystemUser.UserRole.BOOTH_OFFICER).boothId("MUM-B001").build());

        userRepo.save(SystemUser.builder()
                .userId("U002").username("admin").passwordHash(passwordEncoder.encode("admin123"))
                .fullName("Election Commissioner").role(SystemUser.UserRole.ELECTION_ADMIN).boothId(null).build());

        log.info("=== Demo Data Seeded Successfully ===");
        log.info("  Election: GE2024 (ACTIVE)");
        log.info("  Candidates: 4 (Mumbai-North)");
        log.info("  Voters: 15 (with mock Aadhaar)");
        log.info("  Booth Officer: officer1 / officer123");
        log.info("  Admin: admin / admin123");
        log.info("  Swagger UI: http://localhost:{}/swagger-ui.html", port);
        log.info("  H2 Console: http://localhost:{}/h2-console", port);
        log.info("==========================================");
    }

    private void createVoter(String voterId, String name, String aadhaar, String fingerprint, String constituency, String state) {
        // Enroll biometric in mock Aadhaar service
        aadhaarService.enrollMockBiometric(aadhaar, fingerprint);

        // Create voter record (Aadhaar stored as hash, NEVER raw)
        Voter voter = Voter.builder()
                .voterId(voterId)
                .name(name)
                .aadhaarHash(aadhaarService.hashAadhaar(aadhaar))
                .fingerprintHash(aadhaarService.hashFingerprint(fingerprint))
                .constituency(constituency)
                .state(state)
                .build();
        voterRepo.save(voter);
    }

    public DataSeeder(ElectionRepository electionRepo, CandidateRepository candidateRepo, VoterRepository voterRepo, VoteRepository voteRepo, BoothRepository boothRepo, SystemUserRepository userRepo, AadhaarMockService aadhaarService, PasswordEncoder passwordEncoder, VoteEncryptionService encryptionService, HashChainService hashChainService) {
        this.electionRepo = electionRepo;
        this.candidateRepo = candidateRepo;
        this.voterRepo = voterRepo;
        this.voteRepo = voteRepo;
        this.boothRepo = boothRepo;
        this.userRepo = userRepo;
        this.aadhaarService = aadhaarService;
        this.passwordEncoder = passwordEncoder;
        this.encryptionService = encryptionService;
        this.hashChainService = hashChainService;
    }
}
