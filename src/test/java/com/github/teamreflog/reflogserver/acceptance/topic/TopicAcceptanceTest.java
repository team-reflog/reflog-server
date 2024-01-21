package com.github.teamreflog.reflogserver.acceptance.topic;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesRegex;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.github.teamreflog.reflogserver.acceptance.AcceptanceTest;
import com.github.teamreflog.reflogserver.acceptance.auth.AuthFixture;
import com.github.teamreflog.reflogserver.acceptance.member.MemberFixture;
import com.github.teamreflog.reflogserver.acceptance.team.TeamFixture;
import com.github.teamreflog.reflogserver.topic.application.TopicService;
import com.github.teamreflog.reflogserver.topic.domain.DayOfWeekProvider;
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

@DisplayName("인수 테스트: 주제")
class TopicAcceptanceTest extends AcceptanceTest {

    String ownerAccessToken;
    Long teamId;

    @Autowired TopicService service;

    @Override
    @BeforeEach
    protected void setUp() {
        super.setUp();

        MemberFixture.createMember("owner@email.com", "owner");
        ownerAccessToken = AuthFixture.login("owner@email.com", "owner");
        teamId =
                TeamFixture.createTeam(
                        ownerAccessToken,
                        "antifragile",
                        "안티프래질 팀입니다.",
                        "owner",
                        List.of(
                                DayOfWeek.MONDAY,
                                DayOfWeek.WEDNESDAY,
                                DayOfWeek.FRIDAY,
                                DayOfWeek.SUNDAY));
    }

    @Test
    @DisplayName("팀장은 주제를 생성할 수 있다.")
    void createTopic() {
        RestAssured.given()
                .log()
                .all()
                .auth()
                .oauth2(ownerAccessToken)
                .contentType(APPLICATION_JSON_VALUE)
                .body(
                        """
                                {
                                    "teamId": %d,
                                    "content": "오늘 하루는 어땠나요?"
                                }
                                """
                                .formatted(teamId))
                .when()
                .post("/topics")
                .then()
                .log()
                .all()
                .statusCode(201)
                .header(HttpHeaders.LOCATION, matchesRegex("/topics/[0-9]+"));
    }

    @Nested
    @DisplayName("팀장이 주제를 생성하면")
    class WhenOwnerCreateTopic {

        Long firstTopicId, secondTopicId;

        @BeforeEach
        void setUp() {
            firstTopicId = TopicFixture.createTopic(ownerAccessToken, teamId, "오늘 하루는 어땠나요?");
            secondTopicId = TopicFixture.createTopic(ownerAccessToken, teamId, "오늘 무엇을 드셨나요?");
        }

        @Test
        @DisplayName("팀의 주제들을 모두 조회할 수 있다.")
        void queryTopics() {
            RestAssured.given()
                    .log()
                    .all()
                    .auth()
                    .oauth2(ownerAccessToken)
                    .when()
                    .get("/topics?teamId=" + teamId)
                    .then()
                    .log()
                    .all()
                    .statusCode(200)
                    .body("size()", is(2))
                    .body("[0].id", is(firstTopicId.intValue()))
                    .body("[0].teamId", is(teamId.intValue()))
                    .body("[0].content", is("오늘 하루는 어땠나요?"))
                    .body("[1].id", is(secondTopicId.intValue()))
                    .body("[1].teamId", is(teamId.intValue()))
                    .body("[1].content", is("오늘 무엇을 드셨나요?"));
        }

        @Nested
        @DisplayName("팀이 지정한 요일이면")
        class IfTeamDesignatedDay {

            @BeforeEach
            void setUp() {
                DayOfWeekProviderBeanChanger.changeDateProvider(
                        service,
                        new DayOfWeekProvider() {
                            @Override
                            public DayOfWeek getToday(final String timezone) {
                                return DayOfWeek.MONDAY;
                            }
                        });
            }

            @AfterEach
            void tearDown() {
                DayOfWeekProviderBeanChanger.changeDateProvider(service, new DayOfWeekProvider());
            }

            @Test
            @DisplayName("오늘의 주제를 조회할 수 있다.")
            void queryTodayTopic() {
                RestAssured.given()
                        .log()
                        .all()
                        .auth()
                        .oauth2(ownerAccessToken)
                        .header("Time-Zone", "Asia/Seoul")
                        .when()
                        .get("/topics/today")
                        .then()
                        .log()
                        .all()
                        .statusCode(200)
                        .body("size()", is(2))
                        .body("[0].id", is(firstTopicId.intValue()))
                        .body("[0].teamId", is(teamId.intValue()))
                        .body("[0].content", is("오늘 하루는 어땠나요?"))
                        .body("[1].id", is(secondTopicId.intValue()))
                        .body("[1].teamId", is(teamId.intValue()))
                        .body("[1].content", is("오늘 무엇을 드셨나요?"));
            }
        }

        @Nested
        @DisplayName("팀이 지정한 요일이 아니면")
        class IfNotTeamDesignatedDay {

            @BeforeEach
            void setUp() {
                DayOfWeekProviderBeanChanger.changeDateProvider(
                        service,
                        new DayOfWeekProvider() {
                            @Override
                            public DayOfWeek getToday(final String timezone) {
                                return DayOfWeek.TUESDAY;
                            }
                        });
            }

            @AfterEach
            void tearDown() {
                DayOfWeekProviderBeanChanger.changeDateProvider(service, new DayOfWeekProvider());
            }

            @Test
            @DisplayName("오늘의 주제가 보이지 않는다.")
            void queryTodayTopic() {
                RestAssured.given()
                        .log()
                        .all()
                        .auth()
                        .oauth2(ownerAccessToken)
                        .header("Time-Zone", "Asia/Seoul")
                        .when()
                        .get("/topics/today")
                        .then()
                        .log()
                        .all()
                        .statusCode(200)
                        .body("size()", is(0));
            }
        }
    }
}
