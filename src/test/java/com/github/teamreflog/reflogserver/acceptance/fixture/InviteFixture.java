package com.github.teamreflog.reflogserver.acceptance.fixture;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.github.teamreflog.reflogserver.invite.dto.InvitationRequest;
import io.restassured.RestAssured;

public abstract class InviteFixture {

    private InviteFixture() {}

    public static void inviteMember(
            final String accessToken, final String memberEmail, final Long teamId) {
        RestAssured.given()
                .log()
                .all()
                .auth()
                .oauth2(accessToken)
                .body(new InvitationRequest(memberEmail, teamId))
                .contentType(APPLICATION_JSON_VALUE)
                .when()
                .post("/invites")
                .then()
                .log()
                .all()
                .statusCode(200);
    }
}
