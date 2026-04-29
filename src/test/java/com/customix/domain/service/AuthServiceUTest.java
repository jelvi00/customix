package com.customix.domain.service;

import com.customix.config.PasetoManager;
import com.customix.domain.enums.Role;
import com.customix.domain.model.User;
import com.customix.domain.repo.UserRepo;
import com.customix.dto.LoginDTO;
import com.customix.dto.RegistrationDTO;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceUTest {

    @Mock
    private UserRepo userRepo;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private PasetoManager pasetoManager;

    @InjectMocks
    private AuthService authService;

    private final RegistrationDTO.Request testRegRequest;
    private final RegistrationDTO.Request testAdminRegRequest;
    private final LoginDTO.Request testLoginRequest;
    private final User testUser;
    private final User testAdmin;
    private final String encodedPassword;

    public AuthServiceUTest() {
        testRegRequest = new RegistrationDTO.Request(
                "testUser",
                "testPassword",
                null
        );

        testAdminRegRequest = new RegistrationDTO.Request(
                "testAdmin",
                "testPassword",
                Role.ADMIN.name()
        );

        testLoginRequest = new LoginDTO.Request(
                "testUser",
                "testPassword"
        );

        encodedPassword = "encoded_testPassword";

        testUser = User.builder()
                .id(UUID.randomUUID())
                .username("testUser")
                .password(encodedPassword)
                .role(Role.USER)
                .build();
        testUser.setCreatedAt(LocalDateTime.now());

        testAdmin = User.builder()
                .id(UUID.randomUUID())
                .username("testAdmin")
                .password(encodedPassword)
                .role(Role.ADMIN)
                .build();
        testAdmin.setCreatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Given instantiation, then instance obtained")
    void givenInstantiation_ThenInstanceObtained() {

        var service = new AuthService(userRepo, passwordEncoder, pasetoManager);
        assertNotNull(service);

    }

    @Test
    @DisplayName("Given registration, when data is ok and no role provided, then user is created")
    void givenRegistration_whenDataIsOkAndNoRoleProvided_thenUserIsCreated() {

        when(userRepo.existsByUsername(anyString())).thenReturn(false);
        when(userRepo.save(any())).thenReturn(testUser);

        var created = authService.register(testRegRequest);

        assertNotNull(created);
        assertNotNull(created.id());
        assertEquals(testRegRequest.username(), created.username());

    }

    @Test
    @DisplayName("Given registration, when data is ok and role provided, then user is created")
    void givenRegistration_whenDataIsOkAndRoleProvided_thenUserIsCreated() {

        when(userRepo.existsByUsername(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn(testAdmin.getPassword());
        when(userRepo.save(any())).thenReturn(testAdmin);

        var created = authService.register(testAdminRegRequest);

        assertNotNull(created);
        assertNotNull(created.id());
        assertEquals(testAdminRegRequest.username(), created.username());
        assertEquals(Role.ADMIN.name(), created.role());

    }

    @Test
    @DisplayName("Given registration, when invalid request, then exception is thrown")
    void givenRegistration_whenInvalidRequest_thenExceptionIsThrown() {

        Exception exception = assertThrows(IllegalArgumentException.class, () -> authService.register(null));
        assertEquals("Invalid registration.", exception.getMessage());

    }

    @Test
    @DisplayName("Given registration, when provided username is in use, then exception is thrown")
    void givenRegistration_whenProvidedUsernameIsInUse_thenExceptionIsThrown() {

        when(userRepo.existsByUsername(anyString())).thenReturn(true);

        Exception exception = assertThrows(IllegalArgumentException.class, () -> authService.register(testRegRequest));
        assertEquals("Username is not available.", exception.getMessage());

    }

    @Test
    @DisplayName("Given logging in, when username and password are correct, then successfully logged in")
    void givenLoggingIn_whenUsernameAndPasswordAreCorrect_thenSuccessfullyLoggedIn() {

        when(userRepo.findByUsernameAndStatus(anyString(), any())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(true);
        when(pasetoManager.createToken(anyString())).thenReturn("token");

        var loggedIn = authService.login(testLoginRequest.username(), testLoginRequest.password());

        assertNotNull(loggedIn);
        assertEquals(testUser.getId(), loggedIn.id());
        assertEquals(testUser.getUsername(), loggedIn.username());
        assertEquals(testUser.getRole().name(), loggedIn.role());
    }

    @Test
    @DisplayName("Given logging in, when username is missing, then exception is thrown")
    void givenLoggingIn_whenUsernameIsMissing_thenExceptionIsThrown() {

        Exception exception = assertThrows(IllegalArgumentException.class, () -> authService.login(null, "password"));
        assertEquals("Username or password is missing.", exception.getMessage());

    }

    @Test
    @DisplayName("Given logging in, when password is missing, then exception is thrown")
    void givenLoggingIn_whenPasswordIsMissing_thenExceptionIsThrown() {

        Exception exception = assertThrows(IllegalArgumentException.class, () -> authService.login("user", null));
        assertEquals("Username or password is missing.", exception.getMessage());

    }

    @Test
    @DisplayName("Given logging in, when no enabled user by username found, then exception is thrown")
    void givenLoggingIn_whenNoEnabledUserByUsernameFound_thenExceptionIsThrown() {

        when(userRepo.findByUsernameAndStatus(anyString(), any())).thenReturn(Optional.empty());

        Exception exception = assertThrows(
                BadCredentialsException.class,
                () -> authService.login(testLoginRequest.username(), testLoginRequest.password())
        );
        assertEquals("Username or password is incorrect.", exception.getMessage());

    }

    @Test
    @DisplayName("Given logging in, when passwords not matching, then exception is thrown")
    void givenLoggingIn_whenPasswordsNotMatching_thenExceptionIsThrown() {

        when(userRepo.findByUsernameAndStatus(anyString(), any())).thenReturn(Optional.of(testUser));
        when(passwordEncoder.matches(anyString(), anyString())).thenReturn(false);

        Exception exception = assertThrows(
                BadCredentialsException.class,
                () -> authService.login(testLoginRequest.username(), testLoginRequest.password())
        );
        assertEquals("Username or password is incorrect.", exception.getMessage());


    }

}
