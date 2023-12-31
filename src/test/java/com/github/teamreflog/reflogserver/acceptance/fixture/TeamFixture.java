package com.github.teamreflog.reflogserver.acceptance.fixture;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.github.teamreflog.reflogserver.team.dto.TeamCreateRequest;
import io.restassured.RestAssured;
import java.time.DayOfWeek;
import java.util.List;

public abstract class TeamFixture {

    private TeamFixture() {
        /* no-op */
    }

    public static void createTeam(
            final String accessToken,
            final String name,
            final String description,
            final List<DayOfWeek> reflectionDays) {
        RestAssured.given()
                .log()
                .all()
                .auth()
                .oauth2(accessToken)
                .body(new TeamCreateRequest(name, description, reflectionDays))
                .contentType(APPLICATION_JSON_VALUE)
                .when()
                .post("/teams")
                .then()
                .log()
                .all()
                .statusCode(201);
    }
}
