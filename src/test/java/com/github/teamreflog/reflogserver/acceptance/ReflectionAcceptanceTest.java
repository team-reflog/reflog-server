package com.github.teamreflog.reflogserver.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.github.teamreflog.reflogserver.acceptance.fixture.AuthFixture;
import com.github.teamreflog.reflogserver.acceptance.fixture.InviteFixture;
import com.github.teamreflog.reflogserver.acceptance.fixture.MemberFixture;
import com.github.teamreflog.reflogserver.acceptance.fixture.TeamFixture;
import com.github.teamreflog.reflogserver.acceptance.fixture.TopicFixture;
import com.github.teamreflog.reflogserver.reflection.application.dto.ReflectionCreateRequest;
import com.github.teamreflog.reflogserver.reflection.application.dto.ReflectionQueryResponse;
import com.github.teamreflog.reflogserver.topic.infrastructure.DayOfWeekProviderImpl;
import io.restassured.RestAssured;
import java.time.DayOfWeek;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

@DisplayName("인수 테스트: 회고")
public class ReflectionAcceptanceTest extends AcceptanceTest {

    @Autowired DayOfWeekProviderImpl dateProvider;

    String crewToken;
    Long topicId;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();

        /* 오늘은 월요일 */
        dateProvider.setDayOfWeekGenerator(timezone -> DayOfWeek.MONDAY);

        MemberFixture.createMember("owner@email.com", "owner");
        final String ownerToken = AuthFixture.login("owner@email.com", "owner").accessToken();
        final Long teamId =
                TeamFixture.createTeam(
                        ownerToken,
                        "antifragile",
                        "안티프래질 팀입니다.",
                        "나팀장",
                        List.of(
                                DayOfWeek.MONDAY,
                                DayOfWeek.WEDNESDAY,
                                DayOfWeek.FRIDAY,
                                DayOfWeek.SUNDAY));
        topicId = TopicFixture.createTopic(ownerToken, teamId, "오늘 하루는 어땠나요?");

        MemberFixture.createMember("crew@email.com", "crew");
        crewToken = AuthFixture.login("crew@email.com", "crew").accessToken();

        InviteFixture.inviteAndAccept(ownerToken, crewToken, "crew@email.com", teamId);
        TopicFixture.queryTodayTopics(crewToken);
    }

    @AfterEach
    void tearDown() {
        dateProvider.setDefaultDayOfWeekGenerator();
    }

    @Nested
    @DisplayName("회고를 작성할 때")
    class WhenCreateReflection {

        Long reflectionId;

        @BeforeEach
        void setUp() {
            final String reflectionLocation =
                    RestAssured.given()
                            .log()
                            .all()
                            .auth()
                            .oauth2(crewToken)
                            .contentType(APPLICATION_JSON_VALUE)
                            .body(new ReflectionCreateRequest(null, topicId, "힘들었어요 🥲"))
                            .when()
                            .post("/reflections")
                            .then()
                            .log()
                            .all()
                            .statusCode(201)
                            .extract()
                            .header(HttpHeaders.LOCATION)
                            .split("/")[2];

            reflectionId = Long.parseLong(reflectionLocation);
        }

        @Test
        @DisplayName("오늘 쓴 회고를 확인할 수 있다.")
        void queryTodayReflections() {
            final List<ReflectionQueryResponse> responses =
                    RestAssured.given()
                            .log()
                            .all()
                            .auth()
                            .oauth2(crewToken)
                            .header("Time-Zone", "Asia/Seoul")
                            .when()
                            .get("/reflections/today")
                            .then()
                            .log()
                            .all()
                            .statusCode(200)
                            .extract()
                            .body()
                            .jsonPath()
                            .getList(".", ReflectionQueryResponse.class);

            assertThat(responses)
                    .containsExactly(
                            new ReflectionQueryResponse(reflectionId, topicId, "힘들었어요 🥲"));
        }

        // TODO: 회고를 작성한 주제가 Check 표시가 되었는지 확인
        // TODO: 작성한 회고를 조회할 수 있는 지 확인
        // TODO: 회고 확인 (내가 작성한 전체 회고 조회, 특정 팀의 회고 조회, 내가 속한 팀의 회고 조회)
    }
}
