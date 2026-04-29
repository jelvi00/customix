package com.customix.domain.service.admin;

import com.customix.domain.enums.Status;
import com.customix.domain.model.Address;
import com.customix.domain.repo.AddressRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class AddressAdminService {

    private final AddressRepo addressRepo;

    public List<Address> getAllAddresses(Optional<Collection<Status>> statuses) {

        return addressRepo.findByStatusIn(
                statuses.orElse(Collections.singleton(Status.ENABLED))
        );

    }


}
