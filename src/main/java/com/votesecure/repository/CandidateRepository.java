package com.votesecure.repository;

import com.votesecure.model.vote.Candidate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface CandidateRepository extends JpaRepository<Candidate, String> {
    List<Candidate> findByElectionIdAndConstituency(String electionId, String constituency);
    List<Candidate> findByElectionId(String electionId);
}
