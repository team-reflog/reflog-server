package com.github.teamreflog.reflogserver.acceptance.fixture;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.github.teamreflog.reflogserver.member.dto.MemberJoinRequest;
import io.restassured.RestAssured;

public abstract class MemberFixture {

    private MemberFixture() {
        /* no-op */
    }

    public static void createMember(final String email, final String password) {
        RestAssured.given()
                .log()
                .all()
                .body(new MemberJoinRequest(email, password))
                .contentType(APPLICATION_JSON_VALUE)
                .when()
                .post("/members")
                .then()
                .log()
                .all()
                .statusCode(201);
    }
}
