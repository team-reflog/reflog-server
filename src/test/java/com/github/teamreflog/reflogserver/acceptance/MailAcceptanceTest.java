package com.github.teamreflog.reflogserver.acceptance;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

@DisplayName("인수 테스트: 메일")
public class MailAcceptanceTest extends AcceptanceTest {

    @Nested
    @DisplayName("인증 메일을 보낼 때")
    class WhenSendAuthMail {

        String authMailId;

        @BeforeEach
        void setUp() {
            authMailId =
                    RestAssured.given()
                            .log()
                            .all()
                            .contentType(MediaType.APPLICATION_JSON_VALUE)
                            .body(
                                    """
                                    {
                                        "email": "reflog@email.com"
                                    }
                                    """)
                            .when()
                            .post("/mails/send")
                            .then()
                            .log()
                            .all()
                            .statusCode(200)
                            .extract()
                            .body()
                            .jsonPath()
                            .getString("authMailId");
        }

        @Test
        @DisplayName("인증 번호를 검증한다.")
        void verifyAuthNumber() {
            RestAssured.given()
                    .log()
                    .all()
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .body(
                            """
                            {
                                "authMailId": "%s",
                                "authNumber": 240114
                            }
                            """
                                    .formatted(authMailId))
                    .when()
                    .post("/mails/verify")
                    .then()
                    .log()
                    .all()
                    .statusCode(200);
        }
    }
}
