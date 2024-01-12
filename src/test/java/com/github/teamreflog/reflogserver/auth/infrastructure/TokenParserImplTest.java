package com.github.teamreflog.reflogserver.auth.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.github.teamreflog.reflogserver.auth.domain.Token;
import com.github.teamreflog.reflogserver.auth.exception.JwtInvalidException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

// TODO: 테스트 고민, 인프라 테스트는 구현체에 의존하여 테스트한다?
@DisplayName("단위 테스트: TokenParserImpl")
class TokenParserImplTest {

    static final SecretKey SECRET_KEY =
            Keys.hmacShaKeyFor("asdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdf".getBytes());

    TokenParserImpl parser;

    @BeforeEach
    void setUp() {
        parser = new TokenParserImpl(Jwts.parser().verifyWith(SECRET_KEY).build());
    }

    @Nested
    @DisplayName("토큰을 파싱할 때")
    class WhenParse {

        @Test
        @DisplayName("토큰이 유효하면 Jwt를 반환한다")
        void returnJwt() {
            /* given */
            final String token =
                    "Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiIxIiwicm9sZSI6Ik1FTUJFUiIsImV4cCI6OTk5OTk5OTk5OX0.gBhNpEccOhdlwNRg3jcVGTw_fXLY2nKkObcp6P_tUqvbKQvGiLcfGpQzmP9rEPbo";

            /* when */
            final Token jwt = parser.parse(token);

            /* then */
            assertThat(jwt.getSubject()).isEqualTo(1L);
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

        @Test
        @DisplayName("만료된 JWT를 디코딩하면 예외가 발생한다.")
        void throwExceptionWithExpiredJwt() {
            /* given */
            final String expired =
                    "Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiIxIiwicm9sZSI6Ik1FTUJFUiIsImV4cCI6MTcwNTAwMDAwMH0.WnDG4XnyKX9nwl1W1MXggvaj43231oqgxADB4PCzo32RoKReDpMhs3Xpt82jcNbe";

            /* when & then */
            assertThatCode(() -> parser.parse(expired))
                    .isInstanceOf(JwtInvalidException.class)
                    .hasMessage("유효하지 않은 토큰입니다.");
        }

        @Test
        @DisplayName("헤더에 토큰이 없으면 예외가 발생한다.")
        void throwExceptionWithNotExistHeader() {
            /* given & when & then */
            assertAll(
                    () ->
                            assertThatCode(() -> parser.parse(null))
                                    .isInstanceOf(JwtInvalidException.class)
                                    .hasMessage("유효하지 않은 토큰입니다."),
                    () ->
                            assertThatCode(() -> parser.parse(""))
                                    .isInstanceOf(JwtInvalidException.class)
                                    .hasMessage("유효하지 않은 토큰입니다."));
        }

        @Test
        @DisplayName("헤더에 지정된 claim이 없으면 예외가 발생한다.")
        void throwExceptionWithNotExistClaim() {
            /* given */
            final String noSubject =
                    "Bearer eyJhbGciOiJIUzM4NCJ9.eyJyb2xlIjoiTUVNQkVSIiwiZXhwIjo5OTk5OTk5OTk5fQ.fpZCMUnDn5HdBsYsYQC4HYwHnrQManIEfKbYN12QNBqFlxWqCL9hkO7pK6u6oeFA";

            /* when & then */
            assertThatCode(() -> parser.parse(noSubject))
                    .isInstanceOf(JwtInvalidException.class)
                    .hasMessage("유효하지 않은 토큰입니다.");
        }

        @Test
        @DisplayName("토큰이 Bearer로 시작하면 토큰을 추출한다.")
        void extractBearerToken() {
            /* given */
            final String bearerToken = "Bearer ekfrEjrqhRdlaktdlTrpTek";

            /* when */
            final String token = parser.extract(bearerToken);

            /* then */
            assertThat(token).isEqualTo("ekfrEjrqhRdlaktdlTrpTek");
        }

        @Test
        @DisplayName("토큰 타입이 Bearer가 아니면 예외가 발생한다.")
        void throwExceptionWithWrongJwtType() {
            /* given */
            final String noType = "ekfrEjrqhRdlaktdlTrpTek";
            final String wrongType = "Wrong ekfrEjrqhRdlaktdlTrpTek";

            /* when */
            assertAll(
                    () ->
                            assertThatCode(() -> parser.extract(noType))
                                    .isInstanceOf(JwtInvalidException.class)
                                    .hasMessage("유효하지 않은 토큰입니다."),
                    () ->
                            assertThatCode(() -> parser.extract(wrongType))
                                    .isInstanceOf(JwtInvalidException.class)
                                    .hasMessage("유효하지 않은 토큰입니다."));
        }
    }
}
