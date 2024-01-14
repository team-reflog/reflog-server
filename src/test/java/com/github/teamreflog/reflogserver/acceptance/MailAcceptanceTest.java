package com.github.teamreflog.reflogserver.acceptance;

import static org.hamcrest.Matchers.matchesRegex;

import com.github.teamreflog.reflogserver.acceptance.fixture.MailFixture;
import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

@DisplayName("인수 테스트: 메일")
class MailAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("인증 메일 전송을 요청할 수 있다.")
    void sendAuthMail() {
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
                .body(
                        "id",
                        matchesRegex(
                                "[a-f0-9]{8}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{4}-[a-f0-9]{12}")); // UUID
    }

    @Nested
    @DisplayName("인증 메일을 보낼 때")
    class WhenSendAuthMail {

        String authMailId;

        @BeforeEach
        void setUp() {
            authMailId = MailFixture.sendAuthMail("reflog@email.com");
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
                                        "id": "%s",
                                        "code": 240114
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
