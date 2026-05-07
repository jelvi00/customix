package com.customix.domain.service;

import com.customix.domain.enums.Status;
import com.customix.domain.model.Customer;
import com.customix.domain.repo.CustomerRepo;
import com.customix.dto.CustomerDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepo customerRepo;

    public List<Customer> getAllCustomers(Optional<Collection<Status>> statuses) {

        return customerRepo.findAllByStatusIn(statuses.orElse(Collections.singleton(Status.ENABLED)));

    }

    public List<Customer> getCustomersPaged(int page, int size) {

        return customerRepo.findAll(
                PageRequest.of(page, size, Sort.by("status").descending())
        ).getContent();

    }

    public Customer getCustomer(String id) {

        return customerRepo
                .findByIdWithAddresses(UUID.fromString(id))
                .orElse(null);

    }

    public Customer addCustomer(CustomerDTO.AddRequest request) {

        if (customerRepo.existsByEmail(request.email().toLowerCase()))
            throw new IllegalArgumentException("Email is not available.");

        return customerRepo.save(
                Customer.builder()
                        .name(request.name())
                        .email(request.email().toLowerCase())
                        .build()
        );

    }

    public Customer updateCustomer(CustomerDTO.UpdateRequest request) {

        if (Objects.isNull(request.name()) && Objects.isNull(request.email()))
            throw new IllegalArgumentException("Nothing to do.");

        var byIdCustomer = customerRepo.findById(UUID.fromString(request.id()))
                .orElseThrow(() -> new IllegalArgumentException("Customer not found."));

        if (byIdCustomer.getStatus().equals(Status.DISABLED))
            throw new IllegalArgumentException("Customer is already removed.");

        var byEmailCustomer = Objects.nonNull(request.email())
                ? customerRepo.findByEmail(request.email().toLowerCase())
                : null;

        if (Objects.nonNull(byEmailCustomer)
                && !Objects.equals(byEmailCustomer.getId().toString(), byIdCustomer.getId().toString()))
            throw new IllegalArgumentException("Email is not available.");

        if (Objects.nonNull(request.name())) byIdCustomer.setName(request.name());
        if (Objects.nonNull(request.email())) byIdCustomer.setEmail(request.email().toLowerCase());

        return customerRepo.save(byIdCustomer);

    }

    public Customer removeCustomer(String id) {

        var customer = customerRepo.findByIdWithAddresses(UUID.fromString(id))
                .orElseThrow(() -> new IllegalArgumentException("Customer not found"));

        if (customer.getStatus().equals(Status.DISABLED))
            throw new IllegalArgumentException("Customer is already removed.");

        customer.setStatus(Status.DISABLED);
        customer.getAddresses().forEach(address -> address.setStatus(Status.DISABLED));

        return customerRepo.save(customer);
    }

}
