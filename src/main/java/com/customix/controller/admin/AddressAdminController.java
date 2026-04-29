package com.customix.controller.admin;

import com.customix.domain.enums.Status;
import com.customix.domain.model.Address;
import com.customix.domain.service.admin.AddressAdminService;
import com.customix.dto.AddressDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/admin/addresses")
@RequiredArgsConstructor
public class AddressAdminController {

    private final AddressAdminService addressAdminService;

    @GetMapping("")
    public ResponseEntity<List<AddressDTO.Response>> getAllAddresses(
            @Nullable @RequestParam("status") Integer status
    ) {

        var addresses = addressAdminService.getAllAddresses(
                Objects.nonNull(status)
                        ? Optional.of(Collections.singleton(Status.fromId(status)))
                        : Optional.empty()
        );

        return ResponseEntity.ok(addresses.isEmpty()
                ? Collections.emptyList()
                : addresses.stream().map(Address::toDTOResponse).toList()
        );

    }

}
