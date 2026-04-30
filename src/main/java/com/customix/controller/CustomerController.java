package com.customix.controller;

import com.customix.domain.enums.Status;
import com.customix.domain.model.Customer;
import com.customix.domain.service.CustomerService;
import com.customix.dto.CustomerDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/customers")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping()
    public ResponseEntity<List<CustomerDTO.Response>> getCustomers(@Nullable @RequestParam("status") Integer status) {

        var customers = customerService.getAllCustomers(
                Objects.nonNull(status)
                        ? Optional.of(Collections.singleton(Status.fromId(status)))
                        : Optional.empty()
        );

        return ResponseEntity.ok(customers.isEmpty()
                ? Collections.emptyList()
                : customers.stream().map(Customer::toDTOResponse).toList()
        );

    }

    @GetMapping("/{id}/detail")
    public ResponseEntity<CustomerDTO.Response> getCustomer(@PathVariable String id) {

        var customer = customerService.getCustomer(id);

        if (Objects.isNull(customer)) return ResponseEntity.ok().build();
        else return ResponseEntity.ok(customer.toDTOResponse());

    }

    @PostMapping()
    public ResponseEntity<CustomerDTO.Response> addCustomer(@Valid @RequestBody CustomerDTO.AddRequest request) {

        var customer = customerService.addCustomer(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        Objects.isNull(customer)
                                ? null
                                : customer.toDTOResponse()
                );
    }

    @PutMapping()
    public ResponseEntity<CustomerDTO.Response> updateCustomer(@Valid @RequestBody CustomerDTO.UpdateRequest request) {

        var customer = customerService.updateCustomer(request);

        return ResponseEntity.ok(Objects.isNull(customer)
                ? null
                : customer.toDTOResponse()
        );
    }

    @DeleteMapping("/{id}/customer")
    public ResponseEntity<CustomerDTO.Response> removeCustomer(@PathVariable String id) {

        var customer = customerService.removeCustomer(id);

        return ResponseEntity.ok(Objects.isNull(customer)
                ? null
                : customer.toDTOResponse()
        );
    }

}
