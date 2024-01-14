package com.github.teamreflog.reflogserver.acceptance.fixture;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.github.teamreflog.reflogserver.reflection.application.dto.ReflectionCreateRequest;
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
                        .body(new ReflectionCreateRequest(null, topicId, content, null))
                        .when()
                        .post("/reflections")
                        .then()
                        .log()
                        .all()
                        .statusCode(201)
                        .extract()
                        .header(HttpHeaders.LOCATION)
                        .split("/")[2];

        return Long.parseLong(reflectionLocation);
    }
}
