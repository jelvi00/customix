package com.customix.domain.repo;

import com.customix.domain.enums.Status;
import com.customix.domain.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CustomerRepo extends JpaRepository<Customer, UUID> {

    @Query("SELECT c FROM Customer c LEFT JOIN FETCH c.addresses WHERE c.id = :id")
    Optional<Customer> findByIdWithAddresses(@Param("id") UUID id);

    Optional<Customer> findByIdAndStatus(UUID id, Status status);

    Customer findByEmail(String email);

    List<Customer> findAllByStatusIn(Collection<Status> status);

    boolean existsByEmail(String email);

}

