package com.github.teamreflog.reflogserver.acceptance.fixture;

import static org.hamcrest.Matchers.matchesRegex;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.restassured.RestAssured;
import org.springframework.http.HttpHeaders;

public abstract class MemberFixture {

    private MemberFixture() {
        /* no-op */
    }

    public static Long createMember(final String email, final String password) {
        final String memberId =
                RestAssured.given()
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
                        .post("/members")
                        .then()
                        .log()
                        .all()
                        .statusCode(201)
                        .header(LOCATION, matchesRegex("/members/[0-9]+"))
                        .extract()
                        .header(HttpHeaders.LOCATION)
                        .split("/")[2];

        return Long.parseLong(memberId);
    }
}
