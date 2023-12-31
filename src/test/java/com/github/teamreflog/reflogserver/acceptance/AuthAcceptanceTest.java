package com.github.teamreflog.reflogserver.acceptance;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.github.teamreflog.reflogserver.auth.dto.LoginRequest;
import com.github.teamreflog.reflogserver.auth.dto.TokenResponse;
import com.github.teamreflog.reflogserver.member.dto.MemberJoinRequest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

@DisplayName("인수 테스트: 인증")
class AuthAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("회원 가입을 하면 로그인에 성공한다.")
    void createMember() {
        RestAssured.given()
                .log()
                .all()
                .body(new MemberJoinRequest("reflog@email.com", "reflog"))
                .contentType(APPLICATION_JSON_VALUE)
                .when()
                .post("/members")
                .then()
                .log()
                .all()
                .statusCode(201);

        RestAssured.given()
                .log()
                .all()
                .body(new LoginRequest("reflog@email.com", "reflog"))
                .contentType(APPLICATION_JSON_VALUE)
                .when()
                .post("/auth/login")
                .then()
                .log()
                .all()
                .statusCode(200)
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }

    @Test
    @DisplayName("refresh token으로 access token을 재발급 받는다.")
    void refreshToken() {
        RestAssured.given()
                .log()
                .all()
                .body(new MemberJoinRequest("reflog@email.com", "reflog"))
                .contentType(APPLICATION_JSON_VALUE)
                .when()
                .post("/members")
                .then()
                .log()
                .all()
                .statusCode(201);

        final TokenResponse response =
                RestAssured.given()
                        .log()
                        .all()
                        .body(new LoginRequest("reflog@email.com", "reflog"))
                        .contentType(APPLICATION_JSON_VALUE)
                        .when()
                        .post("/auth/login")
                        .then()
                        .log()
                        .all()
                        .statusCode(200)
                        .extract()
                        .body()
                        .as(TokenResponse.class);

        RestAssured.given()
                .log()
                .all()
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + response.refreshToken())
                .when()
                .post("/auth/refresh")
                .then()
                .log()
                .all()
                .statusCode(200)
                .body("accessToken", notNullValue())
                .body("refreshToken", notNullValue());
    }
}
