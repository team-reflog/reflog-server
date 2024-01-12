package com.github.teamreflog.reflogserver.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.github.teamreflog.reflogserver.acceptance.fixture.AuthFixture;
import com.github.teamreflog.reflogserver.acceptance.fixture.InviteFixture;
import com.github.teamreflog.reflogserver.acceptance.fixture.MemberFixture;
import com.github.teamreflog.reflogserver.acceptance.fixture.ReflectionFixture;
import com.github.teamreflog.reflogserver.acceptance.fixture.TeamFixture;
import com.github.teamreflog.reflogserver.acceptance.fixture.TopicFixture;
import com.github.teamreflog.reflogserver.reflection.application.dto.CommentCreateRequest;
import com.github.teamreflog.reflogserver.reflection.application.dto.CommentQueryResponse;
import io.restassured.RestAssured;
import java.time.DayOfWeek;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

@DisplayName("인수 테스트: 댓글")
public class CommentAcceptanceTest extends AcceptanceTest {

    String crewToken;
    Long topicId, reflectionId;

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();

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
        reflectionId = ReflectionFixture.createReflection(crewToken, topicId, "힘들었어요 🥲");
    }

    @Nested
    @DisplayName("댓글을 작성할 때")
    class WhenCreateComment {

        @BeforeEach
        void setUp() {
            RestAssured.given()
                    .log()
                    .all()
                    .auth()
                    .oauth2(crewToken)
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(new CommentCreateRequest(null, null, "오늘 하루도 수고하셨습니다."))
                    .when()
                    .post("/reflections/{reflectionId}/comments", reflectionId)
                    .then()
                    .log()
                    .all()
                    .statusCode(201);
        }

        @Test
        @DisplayName("댓글을 조회할 수 있다.")
        void queryComment() {
            final List<CommentQueryResponse> commentQueryResponses =
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
                            .extract()
                            .body()
                            .jsonPath()
                            .getList(".", CommentQueryResponse.class);

            assertAll(
                    () -> assertThat(commentQueryResponses).hasSize(1),
                    () ->
                            assertThat(commentQueryResponses.get(0).nickname())
                                    .isEqualTo("super-duper-nickname"),
                    () ->
                            assertThat(commentQueryResponses.get(0).content())
                                    .isEqualTo("오늘 하루도 수고하셨습니다."));
        }
    }
}
