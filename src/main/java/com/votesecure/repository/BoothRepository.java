package com.votesecure.repository;

import com.votesecure.model.identity.Booth;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BoothRepository extends JpaRepository<Booth, String> {
    List<Booth> findByIsActiveTrue();
    List<Booth> findByState(String state);
}
