package com.customix.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public class AddressDTO {

    public record AddRequest(
            @NotBlank String customerId,
            @NotBlank String street,
            @NotBlank String city,
            @NotBlank String country,
            String postalCode
    ) {}

    public record UpdateRequest(
            @NotBlank String id,
            String street,
            String city,
            String country,
            String postalCode
    ) {}

    public record Response(
            UUID id,
            String street,
            String city,
            String country,
            String postalCode,
            String status,
            CustomerDTO.Response customer
    ) {}

}
