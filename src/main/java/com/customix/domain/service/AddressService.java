package com.customix.domain.service;

import com.customix.domain.enums.Status;
import com.customix.domain.model.Address;
import com.customix.domain.repo.AddressRepo;
import com.customix.domain.repo.CustomerRepo;
import com.customix.dto.AddressDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AddressService {

    private final AddressRepo addressRepo;
    private final CustomerRepo customerRepo;

    public List<Address> getCustomerAddresses(String id, Optional<Collection<Status>> statuses) {

        return addressRepo.findByCustomerIdAndStatusIn(
                UUID.fromString(id),
                statuses.orElse(Collections.singleton(Status.ENABLED))
        );

    }

    public Address getAddress(String id) {

        return addressRepo
                .findByIdWithCustomer(UUID.fromString(id))
                .orElse(null);

    }

    public Address addAddress(AddressDTO.AddRequest request) {

        var byIdCustomer = customerRepo.findByIdAndStatus(UUID.fromString(request.customerId()), Status.ENABLED)
                .orElseThrow(() -> new IllegalArgumentException("Customer is not available."));

        return addressRepo.save(
                Address.builder()
                        .customer(byIdCustomer)
                        .street(request.street())
                        .city(request.city())
                        .country(request.country())
                        .postalCode(request.postalCode())
                        .build()
        );

    }

    public Address updateAddress(AddressDTO.UpdateRequest request) {

        if (Objects.isNull(request.street()) && Objects.isNull(request.city())
                && Objects.isNull(request.country()) && Objects.isNull(request.postalCode()))
            throw new IllegalArgumentException("Nothing to do.");

        var byIdAddress = addressRepo.findById(UUID.fromString(request.id()))
                .orElseThrow(() -> new IllegalArgumentException("Address not found."));

        if (byIdAddress.getStatus().equals(Status.DISABLED))
            throw new IllegalArgumentException("Address is already removed.");

        if (Objects.nonNull(request.street())) byIdAddress.setStreet(request.street());
        if (Objects.nonNull(request.city())) byIdAddress.setCity(request.city());
        if (Objects.nonNull(request.country())) byIdAddress.setCountry(request.country());
        if (Objects.nonNull(request.postalCode())) byIdAddress.setPostalCode(request.postalCode());

        return addressRepo.save(byIdAddress);

    }

    public Address removeAddress(String id) {

        var address = addressRepo.findById(UUID.fromString(id))
                .orElseThrow(() -> new IllegalArgumentException("Address not found"));

        if (address.getStatus().equals(Status.DISABLED))
            throw new IllegalArgumentException("Address is already removed.");

        address.setStatus(Status.DISABLED);

        return addressRepo.save(address);
    }

}
