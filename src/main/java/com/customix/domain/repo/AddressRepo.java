package com.customix.domain.repo;

import com.customix.domain.enums.Status;
import com.customix.domain.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AddressRepo extends JpaRepository<Address, UUID> {

    @Query("SELECT a FROM Address a LEFT JOIN FETCH a.customer WHERE a.id = :id")
    Optional<Address> findByIdWithCustomer(@Param("id") UUID id);

    List<Address> findByCustomerIdAndStatusIn(UUID customerId, Collection<Status> status);

    List<Address> findByStatusIn(Collection<Status> status);
}
