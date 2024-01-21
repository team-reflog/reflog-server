package com.github.teamreflog.reflogserver.acceptance.member;

import static org.hamcrest.Matchers.matchesRegex;
import static org.springframework.http.HttpHeaders.LOCATION;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

import com.github.teamreflog.reflogserver.acceptance.AcceptanceTest;
import io.restassured.RestAssured;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("인수 테스트: 회원")
class MemberAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("회원 가입을 할 수 있다.")
    void createMember() {
        RestAssured.given()
                .log()
                .all()
                .contentType(APPLICATION_JSON_VALUE)
                .body(
                        """
                                {
                                    "email": "reflog@email.com",
                                    "password": "reflog"
                                }
                                """)
                .when()
                .post("/members")
                .then()
                .log()
                .all()
                .statusCode(201)
                .header(LOCATION, matchesRegex("/members/[0-9]+"));
    }
}
