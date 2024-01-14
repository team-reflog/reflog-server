package com.github.teamreflog.reflogserver.acceptance.reflection;

import static org.hamcrest.Matchers.matchesRegex;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.restassured.RestAssured;

public abstract class CommentFixture {

    private CommentFixture() {
        /* no-op */
    }

    public static Long createComment(
            final String accessToken, final Long reflectionId, final String content) {
        final String commentLocation =
                RestAssured.given()
                        .log()
                        .all()
                        .auth()
                        .oauth2(accessToken)
                        .contentType(APPLICATION_JSON_VALUE)
                        .body(
                                """
                                        {
                                            "content": "%s"
                                        }
                                        """
                                        .formatted(content))
                        .when()
                        .post("/reflections/%d/comments".formatted(reflectionId))
                        .then()
                        .log()
                        .all()
                        .statusCode(201)
                        .header(LOCATION, matchesRegex("/comments/[0-9]+"))
                        .extract()
                        .header(LOCATION);

        return Long.parseLong(commentLocation.split("/")[2]);
    }
}
