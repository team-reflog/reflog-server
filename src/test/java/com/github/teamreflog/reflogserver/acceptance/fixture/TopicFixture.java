package com.github.teamreflog.reflogserver.acceptance.fixture;

import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.github.teamreflog.reflogserver.topic.application.dto.TopicCreateRequest;
import com.github.teamreflog.reflogserver.topic.application.dto.TopicQueryResponse;
import io.restassured.RestAssured;
import java.util.List;

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
                        .body(new TopicCreateRequest(teamId, content))
                        .contentType(APPLICATION_JSON_VALUE)
                        .when()
                        .post("/topics")
                        .then()
                        .log()
                        .all()
                        .statusCode(201)
                        .extract()
                        .header(LOCATION)
                        .split("/")[2];

        return Long.parseLong(topicId);
    }

    public static List<TopicQueryResponse> queryTodayTopics(final String writerAccessToken) {
        return RestAssured.given()
                .log()
                .all()
                .auth()
                .oauth2(writerAccessToken)
                .header("Time-Zone", "Asia/Seoul")
                .when()
                .get("/topics/today")
                .then()
                .log()
                .all()
                .statusCode(200)
                .extract()
                .body()
                .jsonPath()
                .getList(".", TopicQueryResponse.class);
    }
}
