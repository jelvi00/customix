package com.customix.controller;

import com.customix.domain.enums.Role;
import com.customix.domain.service.AuthService;
import com.customix.dto.LoginDTO;
import com.customix.dto.RegistrationDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthControllerUTest {

    @Mock
    private AuthService authService;

    @InjectMocks
    private AuthController authController;

    private final RegistrationDTO.Request testRegRequest;
    private final RegistrationDTO.Response testRegResponse;
    private final LoginDTO.Request testLoginRequest;
    private final LoginDTO.Response testLoginResponse;

    public AuthControllerUTest() {
        testRegRequest = new RegistrationDTO.Request(
                "testUser",
                "testPassword",
                null
        );

        testRegResponse = new RegistrationDTO.Response(
                UUID.fromString("828d8338-566c-4153-b87d-6ec0a099e889"),
                "testUser",
                Role.USER.name(),
                "",
                "",
                ""
        );

        testLoginRequest = new LoginDTO.Request(
                "testUser",
                "testPassword"
        );

        testLoginResponse = new LoginDTO.Response(
                "token",
                UUID.fromString("828d8338-566c-4153-b87d-6ec0a099e889"),
                "testUser",
                "USER"
        );

    }

    @Test
    @DisplayName("Given instantiation, then instance obtained")
    void givenInstantiation_ThenInstanceObtained() {

        var controller = new AuthController(authService);
        assertNotNull(controller);

    }

    @Test
    @DisplayName("Given registration, then successfully registered")
    void givenRegistration_thenSuccessfullyRegistered() {

        when(authService.register(any())).thenReturn(testRegResponse);
        var response = authController.register(testRegRequest);;

        assertNotNull(response);
        assertTrue(response.hasBody());
    }

    @Test
    @DisplayName("Given registration, when invalid request, exceptions is thrown")
    void givenRegistration_whenInvalidRequest_thenExceptionIsThrown() {

        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> authController.register(null)
        );

        assertNotNull(exception);
    }

    @Test
    @DisplayName("Given logging in, then successfully logged in")
    void givenLoggingIn__thenSuccessfullyLoggedIn() {

        when(authService.login(anyString(), anyString())).thenReturn(testLoginResponse);
        var response = authController.login(testLoginRequest);

        assertNotNull(response);
        assertTrue(response.hasBody());
    }

    @Test
    @DisplayName("Given logging in, when invalid credentials, exceptions is thrown")
    void givenLoggingIn_whenInvalidCredentials_thenSuccessfullyLoggedIn() {

        Exception exception = assertThrows(
                IllegalArgumentException.class,
                () -> authController.login(null)
        );

        assertNotNull(exception);

    }

}
