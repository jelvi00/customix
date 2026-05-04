package com.customix.domain.service;

import com.customix.config.security.PasetoManager;
import com.customix.domain.enums.Role;
import com.customix.domain.enums.Status;
import com.customix.domain.model.User;
import com.customix.domain.repo.UserRepo;
import com.customix.dto.LoginDTO;

import com.customix.dto.RegistrationDTO;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AuthService {

    private static final Logger logger = LoggerFactory.getLogger(AuthService.class);

    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;
    private final PasetoManager pasetoManager;

    public RegistrationDTO.Response register(RegistrationDTO.Request request) {

        if (Objects.isNull(request)
                || Objects.isNull(request.username()) || request.username().isBlank()
                || Objects.isNull(request.password()) || request.password().isBlank()
    ) throw new IllegalArgumentException("Invalid registration.");

        if (userRepo.existsByUsername(request.username()))
            throw new IllegalArgumentException("Username is not available.");

        var user = userRepo.save(
                User.builder()
                        .username(request.username())
                        .password(passwordEncoder.encode(request.password()))
                        .role(Objects.nonNull(request.role()) ? Role.valueOf(request.role()) : Role.USER)
                        .build()
        );

        return new RegistrationDTO.Response(
                user.getId(),
                user.getUsername(),
                user.getRole().toString(),
                user.getStatus().toString(),
                user.getCreatedBy(),
                user.getCreatedAt().toString()
        );
    }

    public LoginDTO.Response login(String username, String password) {

        if (Objects.isNull(username) || username.isBlank() || Objects.isNull(password) || password.isBlank())
            throw new IllegalArgumentException("Username or password is missing.");

        var user = userRepo.findByUsernameAndStatus(username, Status.ENABLED)
                .orElseThrow(() -> new BadCredentialsException("Username or password is incorrect."));

        if (!passwordEncoder.matches(password, user.getPassword()))
            throw new BadCredentialsException("Username or password is incorrect.");

        logger.info("Login succeed for user: [{}]", user.getUsername());

        return new LoginDTO.Response(
                pasetoManager.createToken(user.getUsername()),
                user.getId(),
                user.getUsername(),
                user.getRole().name()
        );

    }
}
