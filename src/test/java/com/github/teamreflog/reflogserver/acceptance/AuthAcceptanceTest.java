package com.github.teamreflog.reflogserver.acceptance;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.github.teamreflog.reflogserver.acceptance.fixture.MemberFixture;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;

@DisplayName("인수 테스트: 인증")
class AuthAcceptanceTest extends AcceptanceTest {

    @Nested
    @DisplayName("API를 호출할 때")
    class WhenRequestApi {

        @Test
        @DisplayName("인가 애너테이션이 없는 API는 누구나 호출할 수 있다.")
        void noAuth() {
            RestAssured.given()
                    .log()
                    .all()
                    .when()
                    .get("/auth-acceptance-test/no-auth")
                    .then()
                    .log()
                    .all()
                    .statusCode(200);
        }

        @Test
        @DisplayName("인가 애너테이션에 회원 역할이 있는 API는 JWT 토큰에 회원 역할이 있으면 호출할 수 있다.")
        void memberAuth() {
            RestAssured.given()
                    .log()
                    .all()
                    .header(
                            HttpHeaders.AUTHORIZATION,
                            "Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiIxIiwicm9sZSI6Ik1FTUJFUiIsImV4cCI6OTk5OTk5OTk5OX0.UMg14pIMu_xcvTEtDroRPk1kMUuh45mh3HeRK36fGVaTWKxprdgecBv68OLRGT8z")
                    .when()
                    .get("/auth-acceptance-test/member-auth")
                    .then()
                    .log()
                    .all()
                    .statusCode(200);
        }

        @Test
        @DisplayName("OPTIONS 메서드는 누구나 호출할 수 있다.")
        void options() {
            RestAssured.given()
                    .log()
                    .all()
                    .when()
                    .options("/auth-acceptance-test/preflight")
                    .then()
                    .log()
                    .all()
                    .statusCode(202);
        }

        @Test
        @DisplayName("인가 애너테이션에 역할이 있는 API는 해당 역할이 없으면 호출할 수 없다.")
        void memberAuthFail() {
            RestAssured.given()
                    .log()
                    .all()
                    .header(
                            HttpHeaders.AUTHORIZATION,
                            "Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiIxIiwiZXhwIjo5OTk5OTk5OTk5fQ.keu_gpdcxuDLrrKmY90rVRbC2J-ZbLF6pR574166-S-Ta_ZoSIqewiJTVj77wQm9")
                    .when()
                    .get("/auth-acceptance-test/member-auth")
                    .then()
                    .log()
                    .all()
                    .statusCode(401);
        }
    }

    @Test
    @DisplayName("로그인을 할 수 있다.")
    void createMember() {
        final String email = "reflog@email.com";
        final String password = "reflog";
        MemberFixture.createMember(email, password);

        RestAssured.given()
                .log()
                .all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(
                        """
                        {
                            "email": "%s",
                            "password": "%s"
                        }
                        """
                                .formatted(email, password))
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
    @DisplayName("refresh token으로 token을 재발급 받을 수 있다.")
    void refreshToken() {
        RestAssured.given()
                .log()
                .all()
                .header(
                        HttpHeaders.AUTHORIZATION,
                        "Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiIxIiwicm9sZSI6Ik1FTUJFUiIsImV4cCI6OTk5OTk5OTk5OX0.UMg14pIMu_xcvTEtDroRPk1kMUuh45mh3HeRK36fGVaTWKxprdgecBv68OLRGT8z")
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
