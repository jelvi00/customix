package com.customix.domain.repo;

import com.customix.domain.enums.Status;
import com.customix.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepo extends JpaRepository<User, UUID> {

    Optional<User> findByUsernameAndStatus(String username, Status status);

    boolean existsByUsername(String username);

}
