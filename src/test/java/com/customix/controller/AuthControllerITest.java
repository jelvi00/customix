package com.customix.controller;


import com.customix.domain.enums.Role;
import com.customix.dto.LoginDTO;
import com.customix.dto.RegistrationDTO;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;

public class AuthControllerITest extends ControllerIT {

    private final RegistrationDTO.Request testRegRequest;

    public AuthControllerITest() {
        super();
        testRegRequest = new RegistrationDTO.Request(
                "testUser",
                "testPassword01!",
                null
        );

    }

    @Test
    void shouldRegister() {

        given()
            .contentType("application/json")
            .body(testRegRequest)
        .when()
            .post("/api/auth/registration")
        .then()
            .statusCode(201);


    }

}
