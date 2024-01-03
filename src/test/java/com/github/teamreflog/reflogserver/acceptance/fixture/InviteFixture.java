package com.github.teamreflog.reflogserver.acceptance.fixture;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.github.teamreflog.reflogserver.invite.dto.InviteCreateRequest;
import io.restassured.RestAssured;

public abstract class InviteFixture {

    private InviteFixture() {}

    public static Long inviteMember(
            final String accessToken, final String memberEmail, final Long teamId) {
        final String id =
                RestAssured.given()
                        .log()
                        .all()
                        .auth()
                        .oauth2(accessToken)
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

        return Long.parseLong(id);
    }
}
