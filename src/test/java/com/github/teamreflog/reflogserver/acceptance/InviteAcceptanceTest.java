package com.github.teamreflog.reflogserver.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.github.teamreflog.reflogserver.acceptance.fixture.AuthFixture;
import com.github.teamreflog.reflogserver.acceptance.fixture.InviteFixture;
import com.github.teamreflog.reflogserver.acceptance.fixture.MemberFixture;
import com.github.teamreflog.reflogserver.acceptance.fixture.TeamFixture;
import com.github.teamreflog.reflogserver.invite.dto.InviteAcceptRequest;
import com.github.teamreflog.reflogserver.invite.dto.InviteCreateRequest;
import com.github.teamreflog.reflogserver.invite.dto.InviteQueryResponse;
import com.github.teamreflog.reflogserver.team.dto.TeamMemberQueryResponse;
import io.restassured.RestAssured;
import java.time.DayOfWeek;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("인수 테스트: 초대")
public class InviteAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("팀에 사용자를 초대하면 팀 정보와 사용자 초대 내역에서 확인할 수 있다.")
    void inviteMember() {
        /* given */
        final String memberEmail = "member@email.com";
        MemberFixture.createMember("reflog@email.com", "reflog");
        MemberFixture.createMember(memberEmail, "reflog");
        final String ownerAccessToken =
                AuthFixture.login("reflog@email.com", "reflog").accessToken();
        final String memberAccessToken =
                AuthFixture.login("member@email.com", "reflog").accessToken();

        final Long teamId =
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

        /* when */
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
                .statusCode(201);

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

        /* then */
        assertThat(result).hasSize(1);
        assertThat(result.get(0).teamName()).isEqualTo("antifragile");
    }

    @Test
    @DisplayName("사용자가 팀 초대 요청을 수락한다.")
    void acceptInvitation() {
        /* given */
        final String memberEmail = "member@email.com";
        MemberFixture.createMember("reflog@email.com", "reflog");
        MemberFixture.createMember(memberEmail, "reflog");
        final String ownerAccessToken =
                AuthFixture.login("reflog@email.com", "reflog").accessToken();
        final String memberAccessToken =
                AuthFixture.login("member@email.com", "reflog").accessToken();

        final Long teamId =
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

        final Long inviteId = InviteFixture.inviteMember(ownerAccessToken, memberEmail, teamId);

        /* when */
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

        /* then */
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
