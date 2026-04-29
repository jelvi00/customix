package com.customix.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.UUID;

public class LoginDTO {

    public record Request(
            @NotBlank String username,
            @NotBlank String password
    ) {}

    public record Response(
            String token,
            UUID id,
            String username,
            String role
    ) {}
}
