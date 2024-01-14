package com.github.teamreflog.reflogserver.acceptance.team;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.matchesRegex;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.github.teamreflog.reflogserver.acceptance.AcceptanceTest;
import com.github.teamreflog.reflogserver.acceptance.auth.AuthFixture;
import com.github.teamreflog.reflogserver.acceptance.member.MemberFixture;
import io.restassured.RestAssured;
import java.time.DayOfWeek;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("인수 테스트: 초대")
class InviteAcceptanceTest extends AcceptanceTest {

    String memberEmail, ownerAccessToken, memberAccessToken;
    Long teamId;

    @BeforeEach
    public void setUp() {
        super.setUp();

        memberEmail = "member@email.com";
        MemberFixture.createMember("reflog@email.com", "reflog");
        MemberFixture.createMember(memberEmail, "reflog");
        ownerAccessToken = AuthFixture.login("reflog@email.com", "reflog");
        memberAccessToken = AuthFixture.login("member@email.com", "reflog");
        teamId =
                TeamFixture.createTeam(
                        ownerAccessToken,
                        "antifragile",
                        "안티프래질 팀입니다.",
                        "super-duper-owner",
                        List.of(
                                DayOfWeek.MONDAY,
                                DayOfWeek.WEDNESDAY,
                                DayOfWeek.FRIDAY,
                                DayOfWeek.SUNDAY));
    }

    @Test
    @DisplayName("팀장이 새로운 회원을 초대할 수 있다.")
    void invite() {
        RestAssured.given()
                .log()
                .all()
                .auth()
                .oauth2(ownerAccessToken)
                .contentType(APPLICATION_JSON_VALUE)
                .body(
                        """
                                {
                                    "email": "%s",
                                    "teamId": %d
                                }
                                """
                                .formatted(memberEmail, teamId))
                .when()
                .post("/invites")
                .then()
                .log()
                .all()
                .statusCode(201)
                .header(LOCATION, matchesRegex("/invites/[0-9]+"));
    }

    @Nested
    @DisplayName("팀장이 새로운 회원을 초대할 때")
    class inviteTest {

        Long inviteId;

        @BeforeEach
        void setUp() {
            inviteId = InviteFixture.invite(ownerAccessToken, memberEmail, teamId);
        }

        @Test
        @DisplayName("초대 내역을 확인할 수 있다.")
        void queryInvite() {
            RestAssured.given()
                    .log()
                    .all()
                    .auth()
                    .oauth2(memberAccessToken)
                    .when()
                    .get("/invites")
                    .then()
                    .log()
                    .all()
                    .statusCode(200)
                    .body("size()", is(1))
                    .body("[0].teamName", is("antifragile"));
        }

        @Test
        @DisplayName("초대를 수락할 수 있다.")
        void acceptInvite() {
            RestAssured.given()
                    .log()
                    .all()
                    .auth()
                    .oauth2(memberAccessToken)
                    .contentType(APPLICATION_JSON_VALUE)
                    .body(
                            """
                                    {
                                        "nickname": "super-duper-crew"
                                    }
                                    """)
                    .when()
                    .post("/invites/{inviteId}/accept", inviteId)
                    .then()
                    .log()
                    .all()
                    .statusCode(200);
        }

        @Nested
        @DisplayName("회원이 초대를 수락하면")
        class acceptInviteTest {

            @BeforeEach
            void setUp() {
                InviteFixture.accept(memberAccessToken, inviteId, "super-duper-crew");
            }

            @Test
            @DisplayName("회원의 초대 목록에서 수락한 초대가 삭제된다.")
            void deleteAcceptedInvite() {
                RestAssured.given()
                        .log()
                        .all()
                        .auth()
                        .oauth2(memberAccessToken)
                        .when()
                        .get("/invites")
                        .then()
                        .log()
                        .all()
                        .statusCode(200)
                        .body("size()", is(0));
            }

            @Test
            @DisplayName("수락한 팀 멤버 조회할 시 회원이 조회된다.")
            void queryTeamMember() {
                RestAssured.given()
                        .log()
                        .all()
                        .auth()
                        .oauth2(memberAccessToken)
                        .when()
                        .get("/teams/{teamId}/members", teamId)
                        .then()
                        .log()
                        .all()
                        .statusCode(200)
                        .body("size()", is(2))
                        .body("[0].nickname", is("super-duper-owner"))
                        .body("[1].nickname", is("super-duper-crew"));
            }
        }

        @Test
        @DisplayName("초대를 거절할 수 있다.")
        void rejectInvite() {
            RestAssured.given()
                    .log()
                    .all()
                    .auth()
                    .oauth2(memberAccessToken)
                    .contentType(APPLICATION_JSON_VALUE)
                    .when()
                    .delete("/invites/{inviteId}/reject", inviteId)
                    .then()
                    .log()
                    .all()
                    .statusCode(200);
        }

        @Nested
        @DisplayName("회원이 초대를 거절하면")
        class rejectInviteTest {

            @BeforeEach
            void setUp() {
                InviteFixture.reject(memberAccessToken, inviteId);
            }

            @Test
            @DisplayName("회원의 초대 목록에서 거절한 초대가 삭제된다.")
            void deleteRejectedInvite() {
                RestAssured.given()
                        .log()
                        .all()
                        .auth()
                        .oauth2(memberAccessToken)
                        .when()
                        .get("/invites")
                        .then()
                        .log()
                        .all()
                        .statusCode(200)
                        .body("size()", is(0));
            }

            @Test
            @DisplayName("거절한 팀 멤버 조회할 시 회원이 조회되지 않는다.")
            void queryTeamMember() {
                RestAssured.given()
                        .log()
                        .all()
                        .auth()
                        .oauth2(memberAccessToken)
                        .when()
                        .get("/teams/{teamId}/members", teamId)
                        .then()
                        .log()
                        .all()
                        .statusCode(200)
                        .body("size()", is(1))
                        .body("[0].nickname", is("super-duper-owner"));
            }
        }
    }
}
