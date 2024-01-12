package com.github.teamreflog.reflogserver.auth.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import javax.crypto.SecretKey;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("단위 테스트: JwtProviderImpl")
class JwtProviderImplTest {

    static final SecretKey SECRET_KEY =
            Keys.hmacShaKeyFor("asdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdfasdf".getBytes());

    JwtProviderImpl jwtProviderImpl;

    @BeforeEach
    void setUp() {
        jwtProviderImpl = new JwtProviderImpl(Jwts.builder().signWith(SECRET_KEY));
    }

    @Test
    @DisplayName("access JWT를 생성한다.")
    void createAccessToken() {
        /* given */
        final Long memberId = 777L;

        /* when */
        final String accessToken = jwtProviderImpl.generateAccessToken(memberId);

        /* then */
        assertThat(accessToken).isNotEmpty();
    }

    @Test
    @DisplayName("refresh JWT를 생성한다.")
    void createRefreshToken() {
        /* given */
        final Long memberId = 777L;

        /* when */
        final String refreshToken = jwtProviderImpl.generateRefreshToken(memberId);

        /* then */
        assertThat(refreshToken).isNotEmpty();
    }
}
