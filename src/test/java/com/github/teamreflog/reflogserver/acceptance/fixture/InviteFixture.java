package com.github.teamreflog.reflogserver.acceptance.fixture;

import static org.hamcrest.Matchers.matchesRegex;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import io.restassured.RestAssured;
import org.springframework.http.HttpHeaders;

public abstract class InviteFixture {

    private InviteFixture() {
        /* no-op */
    }

    public static Long invite(
            final String ownerAccessToken, final String memberEmail, final Long teamId) {
        final String inviteLocation =
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
                        .header(LOCATION, matchesRegex("/invites/[0-9]+"))
                        .extract()
                        .header(HttpHeaders.LOCATION);

        return Long.parseLong(inviteLocation.split("/")[2]);
    }

    public static void accept(
            final String inviteeAccessToken, final Long inviteId, final String nickname) {
        RestAssured.given()
                .log()
                .all()
                .auth()
                .oauth2(inviteeAccessToken)
                .contentType(APPLICATION_JSON_VALUE)
                .body(
                        """
                                {
                                                                "nickname": "%s"
                                                            }
                                                            """
                                .formatted(nickname))
                .when()
                .post("/invites/{inviteId}/accept", inviteId)
                .then()
                .log()
                .all()
                .statusCode(200);
    }

    public static void reject(final String inviteeAccessToken, final Long inviteId) {
        RestAssured.given()
                .log()
                .all()
                .auth()
                .oauth2(inviteeAccessToken)
                .contentType(APPLICATION_JSON_VALUE)
                .when()
                .delete("/invites/{inviteId}/reject", inviteId)
                .then()
                .log()
                .all()
                .statusCode(200);
    }

    public static void inviteAndAccept(
            final String inviterAccessToken,
            final String inviteeAccessToken,
            final String memberEmail,
            final Long teamId) {
        final Long inviteId = invite(inviterAccessToken, memberEmail, teamId);
        accept(inviteeAccessToken, inviteId, "super-duper-nickname");
    }
}
