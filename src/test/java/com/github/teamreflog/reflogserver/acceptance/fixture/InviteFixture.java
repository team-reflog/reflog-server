package com.github.teamreflog.reflogserver.acceptance.fixture;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.github.teamreflog.reflogserver.team.application.dto.InviteAcceptRequest;
import com.github.teamreflog.reflogserver.team.application.dto.InviteCreateRequest;
import com.github.teamreflog.reflogserver.team.application.dto.InviteQueryResponse;
import io.restassured.RestAssured;

public abstract class InviteFixture {

    private InviteFixture() {
        /* no-op */
    }

    public static void inviteAndAccept(
            final String inviterAccessToken,
            final String inviteeAccessToken,
            final String memberEmail,
            final Long teamId) {
        RestAssured.given()
                .log()
                .all()
                .auth()
                .oauth2(inviterAccessToken)
                .body(new InviteCreateRequest(null, memberEmail, teamId))
                .contentType(APPLICATION_JSON_VALUE)
                .when()
                .post("/invites")
                .then()
                .log()
                .all()
                .statusCode(201);

        final Long inviteId =
                RestAssured.given()
                        .log()
                        .all()
                        .auth()
                        .oauth2(inviteeAccessToken)
                        .when()
                        .get("/invites")
                        .then()
                        .log()
                        .all()
                        .statusCode(200)
                        .extract()
                        .jsonPath()
                        .getList(".", InviteQueryResponse.class)
                        .get(0)
                        .id();

        RestAssured.given()
                .log()
                .all()
                .auth()
                .oauth2(inviteeAccessToken)
                .body(new InviteAcceptRequest(null, null, "super-duper-nickname"))
                .contentType(APPLICATION_JSON_VALUE)
                .when()
                .post("/invites/{inviteId}/accept", inviteId)
                .then()
                .log()
                .all()
                .statusCode(200)
                .extract();
    }
}
