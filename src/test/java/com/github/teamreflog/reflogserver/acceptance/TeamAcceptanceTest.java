package com.github.teamreflog.reflogserver.acceptance;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.github.teamreflog.reflogserver.acceptance.fixture.AuthFixture;
import com.github.teamreflog.reflogserver.acceptance.fixture.MemberFixture;
import com.github.teamreflog.reflogserver.auth.dto.TokenResponse;
import com.github.teamreflog.reflogserver.team.dto.TeamCreateRequest;
import io.restassured.RestAssured;
import java.time.DayOfWeek;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

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
                                "antifragile",
                                "안티프래질 팀입니다.",
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
                .statusCode(201);
    }
}
