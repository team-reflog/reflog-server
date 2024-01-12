package com.github.teamreflog.reflogserver.acceptance;

import com.github.teamreflog.reflogserver.acceptance.fixture.AuthFixture;
import com.github.teamreflog.reflogserver.acceptance.fixture.InviteFixture;
import com.github.teamreflog.reflogserver.acceptance.fixture.MemberFixture;
import com.github.teamreflog.reflogserver.acceptance.fixture.ReflectionFixture;
import com.github.teamreflog.reflogserver.acceptance.fixture.TeamFixture;
import com.github.teamreflog.reflogserver.acceptance.fixture.TopicFixture;
import io.restassured.RestAssured;
import java.time.DayOfWeek;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

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
                    .auth()
                    .oauth2(crewToken)
                    .when()
                    .post("/reflections/{reflectionId}/comments", reflectionId)
                    .then()
                    .statusCode(201);
        }

        @Test
        @DisplayName("댓글을 조회할 수 있다.")
        void test() {}
    }
}
