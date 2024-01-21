package com.github.teamreflog.reflogserver.acceptance.topic;

import static org.hamcrest.Matchers.matchesRegex;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.restassured.RestAssured;

public abstract class TopicFixture {

    private TopicFixture() {
        /* no- op */
    }

    public static Long createTopic(
            final String accessToken, final Long teamId, final String content) {
        final String topicId =
                RestAssured.given()
                        .log()
                        .all()
                        .auth()
                        .oauth2(accessToken)
                        .contentType(APPLICATION_JSON_VALUE)
                        .body(
                                """
                                        {
                                            "teamId": %d,
                                            "content": "%s"
                                        }
                                        """
                                        .formatted(teamId, content))
                        .when()
                        .post("/topics")
                        .then()
                        .log()
                        .all()
                        .statusCode(201)
                        .header(LOCATION, matchesRegex("/topics/[0-9]+"))
                        .extract()
                        .header(LOCATION)
                        .split("/")[2];

        return Long.parseLong(topicId);
    }
}
