package com.votesecure.repository;

import com.votesecure.model.vote.Election;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ElectionRepository extends JpaRepository<Election, String> {
    List<Election> findByStatus(Election.ElectionStatus status);
}
