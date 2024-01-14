package com.github.teamreflog.reflogserver.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.matchesRegex;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.github.teamreflog.reflogserver.acceptance.fixture.AuthFixture;
import com.github.teamreflog.reflogserver.acceptance.fixture.MemberFixture;
import com.github.teamreflog.reflogserver.acceptance.fixture.TeamFixture;
import com.github.teamreflog.reflogserver.acceptance.fixture.TopicFixture;
import com.github.teamreflog.reflogserver.auth.application.dto.TokenResponse;
import com.github.teamreflog.reflogserver.team.application.dto.CrewQueryResponse;
import com.github.teamreflog.reflogserver.team.application.dto.TeamCreateRequest;
import com.github.teamreflog.reflogserver.team.application.dto.TeamQueryDetailResponse;
import com.github.teamreflog.reflogserver.team.application.dto.TeamQueryResponse;
import io.restassured.RestAssured;
import java.time.DayOfWeek;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

@DisplayName("인수 테스트: 팀")
class TeamAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("팀을 생성한다.")
    void createTeam() {
        MemberFixture.createMember("reflog@email.com", "reflog");
        final TokenResponse tokenResponse = AuthFixture.login("reflog@email.com", "reflog");

        RestAssured.given()
                .log()
                .all()
                .auth()
                .oauth2(tokenResponse.accessToken())
                .body(
                        new TeamCreateRequest(
                                null,
                                "antifragile",
                                "안티프래질 팀입니다.",
                                "owner",
                                List.of(
                                        DayOfWeek.MONDAY,
                                        DayOfWeek.WEDNESDAY,
                                        DayOfWeek.FRIDAY,
                                        DayOfWeek.SUNDAY)))
                .contentType(APPLICATION_JSON_VALUE)
                .when()
                .post("/teams")
                .then()
                .log()
                .all()
                .statusCode(201)
                .header(HttpHeaders.LOCATION, matchesRegex("/teams/[0-9]+"));
    }

    @Test
    @DisplayName("팀 정보를 조회할 수 있다.")
    void queryTeam() {
        final Long memberId = MemberFixture.createMember("reflog@email.com", "reflog");
        final String accessToken = AuthFixture.login("reflog@email.com", "reflog").accessToken();
        final Long teamId =
                TeamFixture.createTeam(
                        accessToken,
                        "antifragile",
                        "안티프래질 팀입니다.",
                        "owner",
                        List.of(
                                DayOfWeek.MONDAY,
                                DayOfWeek.WEDNESDAY,
                                DayOfWeek.FRIDAY,
                                DayOfWeek.SUNDAY));

        RestAssured.given()
                .log()
                .all()
                .auth()
                .oauth2(accessToken)
                .when()
                .get("/teams/" + teamId)
                .then()
                .log()
                .all()
                .statusCode(200)
                .body("name", equalTo("antifragile"))
                .body("description", equalTo("안티프래질 팀입니다."))
                .body("ownerId", equalTo(memberId.intValue()))
                .body(
                        "reflectionDays",
                        equalTo(List.of("MONDAY", "WEDNESDAY", "FRIDAY", "SUNDAY")));
    }

    @Test
    @DisplayName("속한 팀들을 조회할 수 있다.")
    void queryTeams() {
        final Long memberId = MemberFixture.createMember("reflog@email.com", "reflog");
        final String accessToken = AuthFixture.login("reflog@email.com", "reflog").accessToken();
        TeamFixture.createTeam(
                accessToken,
                "anti",
                "안티 팀입니다.",
                "anti-owner",
                List.of(DayOfWeek.MONDAY, DayOfWeek.SUNDAY));
        TeamFixture.createTeam(
                accessToken,
                "fragile",
                "프래질 팀입니다.",
                "fragile-owner",
                List.of(DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY));

        /* when */
        final List<TeamQueryResponse> response =
                RestAssured.given()
                        .log()
                        .all()
                        .auth()
                        .oauth2(accessToken)
                        .when()
                        .get("/teams")
                        .then()
                        .log()
                        .all()
                        .statusCode(200)
                        .extract()
                        .jsonPath()
                        .getList(".", TeamQueryResponse.class);

        /* then */
        assertAll(
                () -> assertThat(response).hasSize(2),
                () -> assertThat(response.get(0).name()).isEqualTo("anti"),
                () -> assertThat(response.get(0).description()).isEqualTo("안티 팀입니다."),
                () -> assertThat(response.get(0).ownerId()).isEqualTo(memberId),
                () ->
                        assertThat(response.get(0).reflectionDays())
                                .containsExactly(DayOfWeek.MONDAY, DayOfWeek.SUNDAY),
                () -> assertThat(response.get(1).name()).isEqualTo("fragile"),
                () -> assertThat(response.get(1).description()).isEqualTo("프래질 팀입니다."),
                () -> assertThat(response.get(1).ownerId()).isEqualTo(memberId),
                () ->
                        assertThat(response.get(1).reflectionDays())
                                .containsExactly(DayOfWeek.WEDNESDAY, DayOfWeek.THURSDAY));
    }

    @Test
    @DisplayName("팀 상세 정보를 조회할 수 있다.")
    void queryTeamDetail() {
        MemberFixture.createMember("owner@email.com", "owner");
        final String ownerAccessToken = AuthFixture.login("owner@email.com", "owner").accessToken();
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
        TopicFixture.createTopic(ownerAccessToken, teamId, "오늘 하루는 어땠나요?");
        TopicFixture.createTopic(ownerAccessToken, teamId, "오늘은 어떤 것을 먹었나요?");

        /* when */
        final TeamQueryDetailResponse response =
                RestAssured.given()
                        .log()
                        .all()
                        .auth()
                        .oauth2(ownerAccessToken)
                        .when()
                        .get("/teams/{teamId}/detail", teamId)
                        .then()
                        .log()
                        .all()
                        .statusCode(200)
                        .extract()
                        .as(TeamQueryDetailResponse.class);

        /* then */
        assertAll(
                () -> assertThat(response.name()).isEqualTo("antifragile"),
                () -> assertThat(response.description()).isEqualTo("안티프래질 팀입니다."),
                () -> assertThat(response.ownerId()).isNotNull(),
                () ->
                        assertThat(response.reflectionDays())
                                .containsExactly(
                                        DayOfWeek.MONDAY,
                                        DayOfWeek.WEDNESDAY,
                                        DayOfWeek.FRIDAY,
                                        DayOfWeek.SUNDAY),
                () -> assertThat(response.topics()).hasSize(2),
                () -> assertThat(response.topics().get(0).content()).isEqualTo("오늘 하루는 어땠나요?"),
                () -> assertThat(response.topics().get(1).content()).isEqualTo("오늘은 어떤 것을 먹었나요?"));
    }

    @Test
    @DisplayName("팀 멤버를 조회한다.")
    void queryCrews() {
        /* given */
        final Long ownerId = MemberFixture.createMember("reflog@email.com", "reflog");
        final String accessToken = AuthFixture.login("reflog@email.com", "reflog").accessToken();
        final Long teamId =
                TeamFixture.createTeam(
                        accessToken,
                        "antifragile",
                        "안티프래질 팀입니다.",
                        "owner",
                        List.of(
                                DayOfWeek.MONDAY,
                                DayOfWeek.WEDNESDAY,
                                DayOfWeek.FRIDAY,
                                DayOfWeek.SUNDAY));

        /* when */
        final List<CrewQueryResponse> response =
                RestAssured.given()
                        .log()
                        .all()
                        .auth()
                        .oauth2(accessToken)
                        .when()
                        .get("/teams/{teamId}/members", teamId)
                        .then()
                        .log()
                        .all()
                        .statusCode(200)
                        .extract()
                        .jsonPath()
                        .getList(".", CrewQueryResponse.class);

        /* then */
        assertThat(response).hasSize(1);
        assertThat(response.get(0).isOwner()).isTrue();
        assertThat(response.get(0).nickname()).isEqualTo("owner");
        assertThat(response.get(0).memberId()).isEqualTo(ownerId);
    }
}
