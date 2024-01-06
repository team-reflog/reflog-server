package com.github.teamreflog.reflogserver.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.github.teamreflog.reflogserver.acceptance.fixture.AuthFixture;
import com.github.teamreflog.reflogserver.acceptance.fixture.MemberFixture;
import com.github.teamreflog.reflogserver.acceptance.fixture.TeamFixture;
import com.github.teamreflog.reflogserver.acceptance.helper.DayOfWeekProviderBeanChanger;
import com.github.teamreflog.reflogserver.topic.application.TopicService;
import com.github.teamreflog.reflogserver.topic.application.dto.TopicCreateRequest;
import com.github.teamreflog.reflogserver.topic.application.dto.TopicQueryResponse;
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
    void setUp() {
        super.setUp();

        MemberFixture.createMember("owner@email.com", "owner");
        ownerAccessToken = AuthFixture.login("owner@email.com", "owner").accessToken();
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

    @Nested
    @DisplayName("팀장이 주제를 생성하면")
    class WhenOwnerCreateTopic {

        Long topicId;

        @BeforeEach
        void setUp() {
            final String topicLocation =
                    RestAssured.given()
                            .log()
                            .all()
                            .auth()
                            .oauth2(ownerAccessToken)
                            .body(new TopicCreateRequest(teamId, "오늘 하루는 어땠나요?"))
                            .contentType(APPLICATION_JSON_VALUE)
                            .when()
                            .post("/topics")
                            .then()
                            .log()
                            .all()
                            .statusCode(201)
                            .extract()
                            .header(HttpHeaders.LOCATION)
                            .replace("/topics/", "");

            topicId = Long.parseLong(topicLocation);
        }

        @Test
        @DisplayName("생성된 주제 목록을 모두 조회할 수 있다.")
        void queryTopics() {
            /* when */
            final List<TopicQueryResponse> responses =
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
                            .extract()
                            .body()
                            .jsonPath()
                            .getList(".", TopicQueryResponse.class);

            /* then */
            assertThat(responses)
                    .containsExactly(new TopicQueryResponse(topicId, teamId, "오늘 하루는 어땠나요?"));
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
                /* when */
                final List<TopicQueryResponse> responses =
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
                                .extract()
                                .body()
                                .jsonPath()
                                .getList(".", TopicQueryResponse.class);

                /* then */
                assertThat(responses)
                        .containsExactly(new TopicQueryResponse(topicId, teamId, "오늘 하루는 어땠나요?"));
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
                /* when */
                final List<TopicQueryResponse> responses =
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
                                .extract()
                                .body()
                                .jsonPath()
                                .getList(".", TopicQueryResponse.class);

                /* then */
                assertThat(responses).isEmpty();
            }
        }
    }
}
