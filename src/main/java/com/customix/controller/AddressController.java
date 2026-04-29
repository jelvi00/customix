package com.customix.controller;

import com.customix.domain.enums.Status;
import com.customix.domain.model.Address;
import com.customix.domain.service.AddressService;
import com.customix.dto.AddressDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final AddressService addressService;

    @GetMapping("/{id}/customer")
    public ResponseEntity<List<AddressDTO.Response>> getCustomerAddresses(
            @PathVariable String id,
            @Nullable @RequestParam("status") Integer status
    ) {

        var addresses = addressService.getCustomerAddresses(
                id,
                Objects.nonNull(status)
                        ? Optional.of(Collections.singleton(Status.fromId(status)))
                        : Optional.empty()
        );

        return ResponseEntity.ok(addresses.isEmpty()
                ? Collections.emptyList()
                : addresses.stream().map(Address::toDTOResponse).toList()
        );

    }

    @GetMapping("/{id}/detail")
    public ResponseEntity<AddressDTO.Response> getAddress(@PathVariable String id) {

        var address = addressService.getAddress(id);

        if (Objects.isNull(address)) return ResponseEntity.ok().build();
        else return ResponseEntity.ok(address.toDTOResponse());

    }

    @PostMapping()
    public ResponseEntity<AddressDTO.Response> addAddress(@Valid @RequestBody AddressDTO.AddRequest request) {

        var address = addressService.addAddress(request);

        return ResponseEntity.ok(Objects.isNull(address)
                ? null
                : address.toDTOResponse()
        );
    }

    @PutMapping()
    public ResponseEntity<AddressDTO.Response> updateAddress(@Valid @RequestBody AddressDTO.UpdateRequest request) {

        var address = addressService.updateAddress(request);

        return ResponseEntity.ok(Objects.isNull(address)
                ? null
                : address.toDTOResponse()
        );
    }

    @DeleteMapping("/{id}/address")
    public ResponseEntity<AddressDTO.Response> removeAddress(@PathVariable String id) {

        var address = addressService.removeAddress(id);

        return ResponseEntity.ok(Objects.isNull(address)
                ? null
                : address.toDTOResponse()
        );
    }

}
