package com.github.teamreflog.reflogserver.acceptance.team;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesRegex;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.github.teamreflog.reflogserver.acceptance.AcceptanceTest;
import com.github.teamreflog.reflogserver.acceptance.auth.AuthFixture;
import com.github.teamreflog.reflogserver.acceptance.member.MemberFixture;
import com.github.teamreflog.reflogserver.acceptance.reflection.ReflectionFixture;
import com.github.teamreflog.reflogserver.acceptance.topic.TopicFixture;
import io.restassured.RestAssured;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

@DisplayName("인수 테스트: 팀")
class TeamAcceptanceTest extends AcceptanceTest {

    Long ownerId;
    String ownerAccessToken;

    @Override
    @BeforeEach
    protected void setUp() {
        super.setUp();

        ownerId = MemberFixture.createMember("reflog@email.com", "reflog");
        ownerAccessToken = AuthFixture.login("reflog@email.com", "reflog");
    }

    @Test
    @DisplayName("팀을 생성할 수 있다.")
    void createTeam() {
        RestAssured.given()
                .log()
                .all()
                .auth()
                .oauth2(ownerAccessToken)
                .contentType(APPLICATION_JSON_VALUE)
                .body(
                        """
                                {
                                    "name": "antifragile",
                                    "description": "안티프래질 팀입니다.",
                                    "nickname": "owner",
                                    "reflectionDays": [
                                        "MONDAY",
                                        "WEDNESDAY",
                                        "FRIDAY",
                                        "SUNDAY"
                                    ]
                                }
                                """)
                .when()
                .post("/teams")
                .then()
                .log()
                .all()
                .statusCode(201)
                .header(HttpHeaders.LOCATION, matchesRegex("/teams/[0-9]+"));
    }

    @Nested
    @DisplayName("팀을 생성하면")
    class WhenCreateTeam {

        Long teamId;
        Long topicId;

        @BeforeEach
        void setUp() {
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

            topicId = TopicFixture.createTopic(ownerAccessToken, teamId, "오늘 하루는 어땠나요?");
        }

        @Test
        @DisplayName("팀 정보를 조회할 수 있다.")
        void queryTeam() {
            RestAssured.given()
                    .log()
                    .all()
                    .auth()
                    .oauth2(ownerAccessToken)
                    .when()
                    .get("/teams/" + teamId)
                    .then()
                    .log()
                    .all()
                    .statusCode(200)
                    .body("name", equalTo("antifragile"))
                    .body("description", equalTo("안티프래질 팀입니다."))
                    .body("ownerId", equalTo(ownerId.intValue()))
                    .body(
                            "reflectionDays",
                            equalTo(List.of("MONDAY", "WEDNESDAY", "FRIDAY", "SUNDAY")));
        }

        @Test
        @DisplayName("팀 멤버를 조회한다.")
        void queryCrews() {
            RestAssured.given()
                    .log()
                    .all()
                    .auth()
                    .oauth2(ownerAccessToken)
                    .when()
                    .get("/teams/{teamId}/members", teamId)
                    .then()
                    .log()
                    .all()
                    .statusCode(200)
                    .body("size()", equalTo(1))
                    .body("[0].isOwner", equalTo(true))
                    .body("[0].nickname", equalTo("owner"))
                    .body("[0].memberId", equalTo(ownerId.intValue()));
        }

        @Test
        @DisplayName("오늘 작성된 팀 회고를 조회한다.")
        void queryTodayTeamReflections() {
            /* given */
            final String content = "힘들었어요 🥲";
            final Long reflectionId =
                    ReflectionFixture.createReflection(ownerAccessToken, topicId, content);

            /* when & then */
            RestAssured.given()
                    .log()
                    .all()
                    .auth()
                    .oauth2(ownerAccessToken)
                    .when()
                    .get("/teams/{teamId}/reflections/today", teamId)
                    .then()
                    .log()
                    .all()
                    .statusCode(200)
                    .body("id", is(teamId.intValue()))
                    .body("name", is("antifragile"))
                    .body("reflections.size()", is(1))
                    .body("reflections[0].nickname", is("owner"))
                    .body("reflections[0].reflectionId", is(reflectionId.intValue()))
                    .body("reflections[0].topicId", is(topicId.intValue()))
                    .body("reflections[0].content", is("힘들었어요 🥲"))
                    .body("reflections[0].reflectionAt", is(LocalDate.now().toString()));
        }
    }

    @Nested
    @DisplayName("팀을 두 개 생성하면")
    class WhenCreateTwoTeam {

        Long firstTeamId, secondTeamId;

        @BeforeEach
        void setUp() {
            firstTeamId =
                    TeamFixture.createTeam(
                            ownerAccessToken,
                            "anti",
                            "안티 팀입니다.",
                            "anti-owner",
                            List.of(DayOfWeek.MONDAY));

            secondTeamId =
                    TeamFixture.createTeam(
                            ownerAccessToken,
                            "fragile",
                            "프래질 팀입니다.",
                            "fragile-owner",
                            List.of(DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY));
        }

        @Test
        @DisplayName("참가한 모든 팀의 정보를 조회할 수 있다.")
        void queryTeams() {
            RestAssured.given()
                    .log()
                    .all()
                    .auth()
                    .oauth2(ownerAccessToken)
                    .when()
                    .get("/teams")
                    .then()
                    .log()
                    .all()
                    .statusCode(200)
                    .body("size()", equalTo(2))
                    .body("[0].id", equalTo(firstTeamId.intValue()))
                    .body("[0].name", equalTo("anti"))
                    .body("[0].description", equalTo("안티 팀입니다."))
                    .body("[0].ownerId", equalTo(ownerId.intValue()))
                    .body("[0].reflectionDays", equalTo(List.of("MONDAY")))
                    .body("[1].id", equalTo(secondTeamId.intValue()))
                    .body("[1].name", equalTo("fragile"))
                    .body("[1].description", equalTo("프래질 팀입니다."))
                    .body("[1].ownerId", equalTo(ownerId.intValue()))
                    .body("[1].reflectionDays", equalTo(List.of("WEDNESDAY", "THURSDAY")));
        }
    }
}
