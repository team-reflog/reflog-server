package com.github.teamreflog.reflogserver.acceptance.reflection;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesRegex;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.github.teamreflog.reflogserver.acceptance.AcceptanceTest;
import com.github.teamreflog.reflogserver.acceptance.auth.AuthFixture;
import com.github.teamreflog.reflogserver.acceptance.member.MemberFixture;
import com.github.teamreflog.reflogserver.acceptance.team.InviteFixture;
import com.github.teamreflog.reflogserver.acceptance.team.TeamFixture;
import com.github.teamreflog.reflogserver.acceptance.topic.DateBasedBeanChanger;
import com.github.teamreflog.reflogserver.acceptance.topic.TopicFixture;
import com.github.teamreflog.reflogserver.reflection.application.ReflectionService;
import com.github.teamreflog.reflogserver.reflection.domain.DateProvider;
import io.restassured.RestAssured;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;

@DisplayName("인수 테스트: 회고")
class ReflectionAcceptanceTest extends AcceptanceTest {

    @Autowired ReflectionService reflectionService;

    String crewToken;
    Long topicId;

    @Override
    @BeforeEach
    protected void setUp() {
        super.setUp();

        DateBasedBeanChanger.changeDateProvider(
                reflectionService,
                new DateProvider() {
                    @Override
                    public LocalDate getLocalDateNow(final String timezone) {
                        return LocalDate.of(2024, 1, 29);
                    }
                });

        MemberFixture.createMember("owner@email.com", "owner");
        final String ownerToken = AuthFixture.login("owner@email.com", "owner");
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
        crewToken = AuthFixture.login("crew@email.com", "crew");

        InviteFixture.inviteAndAccept(ownerToken, crewToken, "crew@email.com", teamId);
    }

    @AfterEach
    void tearDown() {
        DateBasedBeanChanger.changeDateProvider(reflectionService, new DateProvider());
    }

    @Test
    @DisplayName("팀원은 회고를 작성할 수 있다.")
    void createReflection() {
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
                                    "content": "힘들었어요 🥲"
                                }
                                """
                                .formatted(topicId))
                .when()
                .post("/reflections")
                .then()
                .log()
                .all()
                .statusCode(201)
                .header(HttpHeaders.LOCATION, matchesRegex("/reflections/[0-9]+"));
    }

    @Nested
    @DisplayName("회고를 작성할 때")
    class WhenCreateReflection {

        Long reflectionId;

        @BeforeEach
        void setUp() {
            reflectionId = ReflectionFixture.createReflection(crewToken, topicId, "힘들었어요 🥲");
        }

        @Test
        @DisplayName("오늘 쓴 회고를 확인할 수 있다.")
        void queryTodayReflections() {
            RestAssured.given()
                    .log()
                    .all()
                    .auth()
                    .oauth2(crewToken)
                    .header("Time-Zone", "America/New_York")
                    .when()
                    .get("/reflections/today")
                    .then()
                    .log()
                    .all()
                    .statusCode(200)
                    .body("size()", is(1))
                    .body("[0].reflectionId", is(reflectionId.intValue()))
                    .body("[0].topicId", is(topicId.intValue()))
                    .body("[0].content", is("힘들었어요 🥲"));
        }

        // TODO: 회고를 작성한 주제가 Check 표시가 되었는지 확인
        // TODO: 작성한 회고를 조회할 수 있는 지 확인
        // TODO: 회고 확인 (내가 작성한 전체 회고 조회, 특정 팀의 회고 조회, 내가 속한 팀의 회고 조회)
    }
}
