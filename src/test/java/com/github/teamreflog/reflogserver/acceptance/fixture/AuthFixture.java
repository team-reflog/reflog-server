package com.github.teamreflog.reflogserver.acceptance.fixture;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.github.teamreflog.reflogserver.auth.dto.LoginRequest;
import com.github.teamreflog.reflogserver.auth.dto.TokenResponse;
import io.restassured.RestAssured;

public abstract class AuthFixture {

    private AuthFixture() {
        /* no-op */
    }

    public static TokenResponse login(final String email, final String password) {
        return RestAssured.given()
                .log()
                .all()
                .body(new LoginRequest(email, password))
                .contentType(APPLICATION_JSON_VALUE)
                .when()
                .post("/auth/login")
                .then()
                .log()
                .all()
                .statusCode(200)
                .extract()
                .body()
                .as(TokenResponse.class);
    }
}
