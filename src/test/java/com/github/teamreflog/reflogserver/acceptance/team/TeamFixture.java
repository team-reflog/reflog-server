package com.github.teamreflog.reflogserver.acceptance.team;

import static org.hamcrest.Matchers.matchesRegex;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.restassured.RestAssured;
import java.time.DayOfWeek;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.http.HttpHeaders;

public abstract class TeamFixture {

    private TeamFixture() {
        /* no-op */
    }

    public static Long createTeam(
            final String accessToken,
            final String name,
            final String description,
            final String nickname,
            final List<DayOfWeek> reflectionDays) {
        final String teamLocation =
                RestAssured.given()
                        .log()
                        .all()
                        .auth()
                        .oauth2(accessToken)
                        .contentType(APPLICATION_JSON_VALUE)
                        .body(
                                """
                                        {
                                            "name": "%s",
                                            "description": "%s",
                                            "nickname": "%s",
                                            "reflectionDays": %s
                                        }
                                        """
                                        .formatted(
                                                name,
                                                description,
                                                nickname,
                                                toString(reflectionDays)))
                        .when()
                        .post("/teams")
                        .then()
                        .log()
                        .all()
                        .statusCode(201)
                        .header(HttpHeaders.LOCATION, matchesRegex("/teams/[0-9]+"))
                        .extract()
                        .header(HttpHeaders.LOCATION);

        return Long.parseLong(teamLocation.split("/")[2]);
    }

    private static String toString(final List<DayOfWeek> reflectionDays) {
        return reflectionDays.stream()
                .map(day -> "\"" + day + "\"")
                .collect(Collectors.joining(", ", "[", "]"));
    }
}
