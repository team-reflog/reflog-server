package com.github.teamreflog.reflogserver.acceptance.fixture;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.restassured.RestAssured;

public abstract class AuthFixture {

    private AuthFixture() {
        /* no-op */
    }

    public static String login(final String email, final String password) {
        return RestAssured.given()
                .log()
                .all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(
                        """
                        {
                            "email": "%s",
                            "password": "%s"
                        }
                        """
                                .formatted(email, password))
                .when()
                .post("/auth/login")
                .then()
                .log()
                .all()
                .statusCode(200)
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue())
                .extract()
                .body()
                .jsonPath()
                .getString("accessToken");
    }
}
