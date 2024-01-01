package com.github.teamreflog.reflogserver.acceptance;

import static org.hamcrest.Matchers.matchesRegex;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.github.teamreflog.reflogserver.acceptance.fixture.AuthFixture;
import com.github.teamreflog.reflogserver.acceptance.fixture.MemberFixture;
import com.github.teamreflog.reflogserver.acceptance.fixture.TeamFixture;
import com.github.teamreflog.reflogserver.topic.dto.TopicCreateRequest;
import io.restassured.RestAssured;
import java.time.DayOfWeek;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

@DisplayName("인수 테스트: 주제")
class TopicAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("주제를 생성한다.")
    void createTopic() {
        /* given */
        final Long memberId = MemberFixture.createMember("reflog@email.com", "reflog");
        final String accessToken = AuthFixture.login("reflog@email.com", "reflog").accessToken();
        final Long teamId =
                TeamFixture.createTeam(
                        accessToken,
                        "antifragile",
                        "안티프래질 팀입니다.",
                        List.of(
                                DayOfWeek.MONDAY,
                                DayOfWeek.WEDNESDAY,
                                DayOfWeek.FRIDAY,
                                DayOfWeek.SUNDAY));

        RestAssured.given()
                .log()
                .all()
                .auth()
                .oauth2(accessToken)
                .body(new TopicCreateRequest(teamId, "오늘 하루는 어땠나요?"))
                .contentType(APPLICATION_JSON_VALUE)
                .when()
                .post("/topics")
                .then()
                .log()
                .all()
                .statusCode(201)
                .header(HttpHeaders.LOCATION, matchesRegex("/topics/[0-9]+"));
    }
}
