package com.github.teamreflog.reflogserver.auth.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.github.teamreflog.reflogserver.auth.exception.JwtInvaildException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("단위 테스트: JwtProvider")
class JwtProviderTest {

    JwtProvider jwtProvider;

    @BeforeEach
    void setUp() {
        jwtProvider = new JwtProvider("6rOo66qp7Iud64u564ut65ah67O27J20");
    }

    @Test
    @DisplayName("access JWT를 생성한다.")
    void createAccessToken() {
        /* given */
        final Long memberId = 777L;

        /* when */
        final String token = jwtProvider.generateAccessToken(memberId);
        final Long subject = jwtProvider.decode(token);

        /* then */
        assertThat(subject).isEqualTo(memberId);
    }

    @Test
    @DisplayName("refresh JWT를 생성한다.")
    void createRefreshToken() {
        /* given */
        final Long memberId = 777L;

        /* when */
        final String token = jwtProvider.generateRefreshToken(memberId);
        final Long subject = jwtProvider.decode(token);

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
        assertThatCode(() -> jwtProvider.decode(expired))
                .isInstanceOf(JwtInvaildException.class)
                .hasMessage("유효하지 않은 토큰입니다.");
    }
}
