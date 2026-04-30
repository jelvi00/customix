package com.customix.controller;

import com.customix.domain.repo.UserRepo;
import com.customix.dto.LoginDTO;
import com.customix.dto.RegistrationDTO;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isA;

public class AuthControllerITest extends ControllerIT {

    @Autowired
    private UserRepo userRepo;

    private final RegistrationDTO.Request testRegRequest;
    private final LoginDTO.Request testLoginRequest;

    public AuthControllerITest() {
        super();
        testRegRequest = new RegistrationDTO.Request(
                "testUser",
                "testPassword01!",
                null
        );

        testLoginRequest = new LoginDTO.Request(
                "testUser",
                "testPassword01!"
        );

    }

    @AfterEach
    void tearDown() {
        userRepo.deleteAll();
    }

    @Test
    void shouldSuccessfullyRegister() {

        given()
            .contentType("application/json")
            .body(testRegRequest)
        .when()
            .post("/api/auth/registration")
        .then()
            .statusCode(HttpStatus.CREATED.value())
            .body("role", is("USER"));

    }

    @Test
    void shouldSuccessfullyLogin() {

        given()
            .contentType("application/json")
            .body(testRegRequest)
        .when()
            .post("/api/auth/registration");

        given()
            .contentType("application/json")
            .body(testLoginRequest)
        .when()
            .post("/api/auth/login")
        .then()
            .statusCode(HttpStatus.OK.value())
            .body("token", isA(String.class));

    }

}
