package com.github.teamreflog.reflogserver.acceptance.fixture;

import static org.hamcrest.Matchers.matchesRegex;

import io.restassured.RestAssured;
import org.springframework.http.MediaType;

public abstract class MailFixture {

    private MailFixture() {
        /* no-op */
    }

    public static String sendAuthMail(final String email) {
        return RestAssured.given()
                .log()
                .all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(
                        """
                        {
                            "email": "%s"
                        }
                        """
                                .formatted(email))
                .when()
                .post("/mails/send")
                .then()
                .log()
                .all()
                .statusCode(200)
                .body(
                        "id",
                        matchesRegex(
                                "[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}")) // UUID
                .extract()
                .body()
                .jsonPath()
                .getString("id");
    }
}
