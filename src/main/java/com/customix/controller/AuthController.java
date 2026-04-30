package com.customix.controller;

import com.customix.domain.service.AuthService;
import com.customix.dto.LoginDTO;
import com.customix.dto.RegistrationDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/registration")
    public ResponseEntity<RegistrationDTO.Response> register(@Valid @RequestBody RegistrationDTO.Request request) {

        if (Objects.isNull(request)) throw new IllegalArgumentException();
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginDTO.Response> login(@Valid @RequestBody LoginDTO.Request request) {

        if (Objects.isNull(request)) throw new IllegalArgumentException();
        return ResponseEntity.ok(authService.login(request.username(), request.password()));
    }
}
