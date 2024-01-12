package com.github.teamreflog.reflogserver.auth.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.github.teamreflog.reflogserver.auth.domain.ClaimType;
import com.github.teamreflog.reflogserver.auth.domain.Jwt;
import com.github.teamreflog.reflogserver.auth.domain.JwtParser;
import com.github.teamreflog.reflogserver.auth.exception.JwtInvalidException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

// TODO: 테스트 고민, 인프라 테스트는 구현체에 의존하여 테스트한다?
@DisplayName("단위 테스트: JwtParserImpl")
class JwtParserImplTest {

    static SecretKey secret =
            Keys.hmacShaKeyFor("asdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdf".getBytes());

    JwtParser parser;

    @BeforeEach
    void setUp() {
        parser = new JwtParserImpl(Jwts.parser().verifyWith(secret).build());
    }

    @Nested
    @DisplayName("토큰을 파싱할 때")
    class WhenParse {

        @Test
        @DisplayName("토큰이 유효하면 Jwt를 반환한다")
        void returnJwt() {
            /* given */
            final String token =
                    "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiIxIiwicm9sZSI6Ik1FTUJFUiIsImV4cCI6OTk5OTk5OTk5OX0.gBhNpEccOhdlwNRg3jcVGTw_fXLY2nKkObcp6P_tUqvbKQvGiLcfGpQzmP9rEPbo";

            /* when */
            final Jwt jwt = parser.parse(token);

            /* then */
            assertAll(
                    () -> assertThat(jwt.getClaim(ClaimType.MEMBER_ID)).isEqualTo("1"),
                    () -> assertThat(jwt.getClaim(ClaimType.ROLE)).isEqualTo("MEMBER"));
        }

        @Test
        @DisplayName("토큰이 유효하지 않으면 예외를 발생시킨다.")
        void throwException() {
            /* given */
            final String token =
                    "eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiIxIiwicm9sZSI6Ik1FTUJFUiIsImV4cCI6OTk5OTk5OTk5OX0.gBhNpEccOhdlwNRg3jcVGTw_fXLY2nKkObcp6P_tUqvbKQvGiLcfGpQzmasdf";

            /* when, then */
            assertThatCode(() -> parser.parse("token"))
                    .isInstanceOf(JwtInvalidException.class)
                    .hasMessage("유효하지 않은 토큰입니다.");
        }
    }
}
