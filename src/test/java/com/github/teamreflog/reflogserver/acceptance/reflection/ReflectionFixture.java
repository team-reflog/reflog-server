package com.github.teamreflog.reflogserver.acceptance.reflection;

import static org.hamcrest.Matchers.matchesRegex;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.restassured.RestAssured;
import org.springframework.http.HttpHeaders;

public abstract class ReflectionFixture {

    private ReflectionFixture() {
        /* no-op */
    }

    public static Long createReflection(
            final String crewToken, final Long topicId, final String content) {
        final String reflectionLocation =
                RestAssured.given()
                        .log()
                        .all()
                        .auth()
                        .oauth2(crewToken)
                        .header("Time-Zone", "America/New_York")
                        .contentType(APPLICATION_JSON_VALUE)
                        .body(
                                """
                                        {
                                            "topicId": %d,
                                            "content": "%s"
                                        }
                                        """
                                        .formatted(topicId, content))
                        .when()
                        .post("/reflections")
                        .then()
                        .log()
                        .all()
                        .statusCode(201)
                        .header(HttpHeaders.LOCATION, matchesRegex("/reflections/[0-9]+"))
                        .extract()
                        .header(HttpHeaders.LOCATION)
                        .split("/")[2];

        return Long.parseLong(reflectionLocation);
    }
}
