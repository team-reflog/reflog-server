package com.github.teamreflog.reflogserver.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.github.teamreflog.reflogserver.acceptance.fixture.AuthFixture;
import com.github.teamreflog.reflogserver.acceptance.fixture.MemberFixture;
import com.github.teamreflog.reflogserver.acceptance.fixture.TeamFixture;
import com.github.teamreflog.reflogserver.invite.dto.InviteAcceptRequest;
import com.github.teamreflog.reflogserver.invite.dto.InviteCreateRequest;
import com.github.teamreflog.reflogserver.invite.dto.InviteQueryResponse;
import com.github.teamreflog.reflogserver.team.dto.TeamMemberQueryResponse;
import io.restassured.RestAssured;
import java.time.DayOfWeek;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("인수 테스트: 초대")
public class InviteAcceptanceTest extends AcceptanceTest {

    String memberEmail, ownerAccessToken, memberAccessToken;
    Long teamId;

    @BeforeEach
    void setUp() {
        memberEmail = "member@email.com";
        MemberFixture.createMember("reflog@email.com", "reflog");
        MemberFixture.createMember(memberEmail, "reflog");
        ownerAccessToken = AuthFixture.login("reflog@email.com", "reflog").accessToken();
        memberAccessToken = AuthFixture.login("member@email.com", "reflog").accessToken();
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
    @DisplayName("팀장이 새로운 회원을 초대할 때")
    class inviteTest {

        Long inviteId;

        @BeforeEach
        void setUp() {
            final String id =
                    RestAssured.given()
                            .log()
                            .all()
                            .auth()
                            .oauth2(ownerAccessToken)
                            .body(new InviteCreateRequest(memberEmail, teamId))
                            .contentType(APPLICATION_JSON_VALUE)
                            .when()
                            .post("/invites")
                            .then()
                            .log()
                            .all()
                            .statusCode(201)
                            .extract()
                            .header("Location")
                            .split("/")[2];
            inviteId = Long.parseLong(id);
        }

        @Test
        @DisplayName("헤더에 초대 번호 정보를 담아서 응답한다.")
        void headerContainsInviteId() {
            assertThat(inviteId).isPositive();
        }

        @Test
        @DisplayName("초대 내역을 확인할 수 있다.")
        void queryInvite() {
            final List<InviteQueryResponse> result =
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
                            .extract()
                            .jsonPath()
                            .getList(".", InviteQueryResponse.class);

            assertThat(result).hasSize(1);
            assertThat(result.get(0).teamName()).isEqualTo("antifragile");
        }

        @Nested
        @DisplayName("회원이 초대를 수락하면")
        class acceptInviteTest {

            @BeforeEach
            void setUp() {
                RestAssured.given()
                        .log()
                        .all()
                        .auth()
                        .oauth2(memberAccessToken)
                        .body(new InviteAcceptRequest("user"))
                        .contentType(APPLICATION_JSON_VALUE)
                        .when()
                        .post("/invites/{inviteId}/accept", inviteId)
                        .then()
                        .log()
                        .all()
                        .statusCode(200)
                        .extract();
            }

            @Test
            @DisplayName("회원의 초대 목록에서 수락한 초대가 삭제된다.")
            void deleteAcceptedInvite() {
                final List<InviteQueryResponse> result =
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
                                .extract()
                                .jsonPath()
                                .getList(".", InviteQueryResponse.class);

                assertThat(result).isEmpty();
            }

            @Test
            @DisplayName("수락한 팀 멤버 조회할 시 회원이 조회된다.")
            void queryTeamMebmer() {
                final List<TeamMemberQueryResponse> teamMembers =
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
                                .extract()
                                .jsonPath()
                                .getList(".", TeamMemberQueryResponse.class);

                assertThat(teamMembers).hasSize(2);
                assertThat(teamMembers).extracting("nickname").contains("owner", "user");
            }
        }
    }
}
