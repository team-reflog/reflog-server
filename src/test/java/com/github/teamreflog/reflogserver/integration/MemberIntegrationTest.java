package com.github.teamreflog.reflogserver.integration;

import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.github.teamreflog.reflogserver.auth.dto.LoginRequest;
import com.github.teamreflog.reflogserver.member.dto.MemberJoinRequest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class MemberIntegrationTest {

    @LocalServerPort int port;

    @BeforeEach
    void setUp() {
        RestAssured.port = port;
    }

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
                .body("token", notNullValue());
    }
}
