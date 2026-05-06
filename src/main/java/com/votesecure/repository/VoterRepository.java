package com.votesecure.repository;

import com.votesecure.model.identity.Voter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface VoterRepository extends JpaRepository<Voter, String> {
    Optional<Voter> findByAadhaarHash(String aadhaarHash);
    long countByHasVotedTrue();
    long countByConstituencyAndHasVotedTrue(String constituency);
    long countByConstituency(String constituency);
}
