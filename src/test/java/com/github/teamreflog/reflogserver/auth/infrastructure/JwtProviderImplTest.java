package com.github.teamreflog.reflogserver.auth.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.github.teamreflog.reflogserver.auth.exception.JwtInvalidException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("단위 테스트: JwtProviderImpl")
class JwtProviderImplTest {

    JwtProviderImpl jwtProviderImpl;

    @BeforeEach
    void setUp() {
        jwtProviderImpl = new JwtProviderImpl("6rOo66qp7Iud64u564ut65ah67O27J2066eb7J6I6rKg64uk");
    }

    @Test
    @DisplayName("access JWT를 생성한다.")
    void createAccessToken() {
        /* given */
        final Long memberId = 777L;

        /* when */
        final String token = jwtProviderImpl.generateAccessToken(memberId);
        final Long subject = jwtProviderImpl.parseSubject(token);

        /* then */
        assertThat(subject).isEqualTo(memberId);
    }

    @Test
    @DisplayName("refresh JWT를 생성한다.")
    void createRefreshToken() {
        /* given */
        final Long memberId = 777L;

        /* when */
        final String token = jwtProviderImpl.generateRefreshToken(memberId);
        final Long subject = jwtProviderImpl.parseSubject(token);

        /* then */
        assertThat(subject).isEqualTo(memberId);
    }

    @Test
    @DisplayName("만료된 JWT를 디코딩하면 예외가 발생한다.")
    void throwExceptionWithExpiredJwt() {
        /* given */
        final String expired =
                "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiI3NzciLCJleHAiOjE3MDQwMTkwNTV9.Gq8SgcYQEPv_W7yKpzHbCQPDGx9YyllLDD6f8vo8p_Q";

        /* when & then */
        assertThatCode(() -> jwtProviderImpl.parseSubject(expired))
                .isInstanceOf(JwtInvalidException.class)
                .hasMessage("유효하지 않은 토큰입니다.");
    }

    @Test
    @DisplayName("헤더에서 토큰을 추출한다.")
    void extractJwt() {
        /* given */
        final String header = "Bearer ekfrEjrqhRdlaktdlTrpTek";

        /* when */
        final String jwt = jwtProviderImpl.extractToken(header);

        /* then */
        assertThat(jwt).isEqualTo("ekfrEjrqhRdlaktdlTrpTek");
    }

    @Test
    @DisplayName("헤더에 토큰이 없으면 예외가 발생한다.")
    void throwExceptionWithNotExistHeader() {
        /* given & when & then */
        assertAll(
                () ->
                        assertThatCode(() -> jwtProviderImpl.extractToken(null))
                                .isInstanceOf(JwtInvalidException.class)
                                .hasMessage("유효하지 않은 토큰입니다."),
                () ->
                        assertThatCode(() -> jwtProviderImpl.extractToken(""))
                                .isInstanceOf(JwtInvalidException.class)
                                .hasMessage("유효하지 않은 토큰입니다."));
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
                        assertThatCode(() -> jwtProviderImpl.extractToken(noType))
                                .isInstanceOf(JwtInvalidException.class)
                                .hasMessage("유효하지 않은 토큰입니다."),
                () ->
                        assertThatCode(() -> jwtProviderImpl.extractToken(wrongType))
                                .isInstanceOf(JwtInvalidException.class)
                                .hasMessage("유효하지 않은 토큰입니다."));
    }
}
