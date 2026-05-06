package com.votesecure.repository;

import com.votesecure.model.vote.EncryptedVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.UUID;

@Repository
public interface VoteRepository extends JpaRepository<EncryptedVote, UUID> {
    List<EncryptedVote> findByElectionIdOrderByVoteTimestampAsc(String electionId);
    long countByElectionId(String electionId);
    long countByElectionIdAndConstituency(String electionId, String constituency);
}
