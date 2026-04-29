package com.customix.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

import java.util.List;
import java.util.UUID;

public class CustomerDTO {

    public record AddRequest(
            @NotBlank @Size(min = 3, max = 60) String name,
            @NotBlank @Email String email
    ) {}

    public record UpdateRequest(
            @NotBlank String id,
            @Size(min = 3, max = 60) String name,
            @Email String email
    ) {}

    public record Response(
            UUID id,
            String name,
            String email,
            String status,
            List<AddressDTO.Response> addresses
    ) {}

}
