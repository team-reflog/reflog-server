package com.github.teamreflog.reflogserver.acceptance;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.github.teamreflog.reflogserver.acceptance.fixture.AuthFixture;
import com.github.teamreflog.reflogserver.acceptance.fixture.MemberFixture;
import com.github.teamreflog.reflogserver.auth.application.dto.LoginRequest;
import com.github.teamreflog.reflogserver.auth.application.dto.TokenResponse;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

@DisplayName("인수 테스트: 인증")
class AuthAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("회원 가입을 하면 로그인에 성공한다.")
    void createMember() {
        MemberFixture.createMember("reflog@email.com", "reflog");

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
    @DisplayName("refresh token으로 token을 재발급 받는다.")
    void refreshToken() {
        MemberFixture.createMember("reflog@email.com", "reflog");
        final TokenResponse response = AuthFixture.login("reflog@email.com", "reflog");

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
