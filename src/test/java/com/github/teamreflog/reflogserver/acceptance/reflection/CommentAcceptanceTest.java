package com.github.teamreflog.reflogserver.acceptance.reflection;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesRegex;

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
import org.springframework.http.MediaType;

@DisplayName("인수 테스트: 댓글")
class CommentAcceptanceTest extends AcceptanceTest {

    @Autowired ReflectionService reflectionService;

    String crewToken;
    Long topicId, reflectionId;

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
        reflectionId = ReflectionFixture.createReflection(crewToken, topicId, "힘들었어요 🥲");
    }

    @AfterEach
    void tearDown() {
        DateBasedBeanChanger.changeDateProvider(reflectionService, new DateProvider());
    }

    @Test
    @DisplayName("팀원은 회고에 댓글을 작성할 수 있다.")
    void createComment() {
        RestAssured.given()
                .log()
                .all()
                .auth()
                .oauth2(crewToken)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(
                        """
                                {
                                    "content": "오늘 하루도 수고하셨습니다."
                                }
                                """)
                .when()
                .post("/reflections/{reflectionId}/comments", reflectionId)
                .then()
                .log()
                .all()
                .statusCode(201)
                .header(HttpHeaders.LOCATION, matchesRegex("/comments/[0-9]+"));
    }

    @Nested
    @DisplayName("댓글을 작성할 때")
    class WhenCreateComment {

        @BeforeEach
        void setUp() {
            CommentFixture.createComment(crewToken, reflectionId, "오늘 하루도 수고하셨습니다.");
        }

        @Test
        @DisplayName("댓글을 조회할 수 있다.")
        void queryComment() {
            RestAssured.given()
                    .log()
                    .all()
                    .auth()
                    .oauth2(crewToken)
                    .when()
                    .get("/reflections/{reflectionId}/comments", reflectionId)
                    .then()
                    .log()
                    .all()
                    .statusCode(200)
                    .body("size()", is(1))
                    .body("[0].nickname", is("super-duper-nickname"))
                    .body("[0].content", is("오늘 하루도 수고하셨습니다."));
        }
    }
}
