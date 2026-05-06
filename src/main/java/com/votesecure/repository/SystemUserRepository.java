package com.votesecure.repository;

import com.votesecure.model.auth.SystemUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface SystemUserRepository extends JpaRepository<SystemUser, String> {
    Optional<SystemUser> findByUsername(String username);
    boolean existsByUsername(String username);
}
