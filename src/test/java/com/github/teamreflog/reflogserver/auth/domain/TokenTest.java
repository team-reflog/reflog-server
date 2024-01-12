package com.github.teamreflog.reflogserver.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.github.teamreflog.reflogserver.auth.exception.JwtInvalidException;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("단위 테스트: Token")
class TokenTest {

    @Test
    @DisplayName("토큰에서 클레임을 추출한다.")
    void getClaim() {
        /* given */
        final Token token =
                Token.from(
                        Map.of(
                                ClaimType.MEMBER_ID, "1",
                                ClaimType.ROLE, "MEMBER"));

        /* when & then */
        assertThat(token.getSubject()).isEqualTo(1L);
    }

    @Test
    @DisplayName("토큰에 모든 클레임이 없으면 예외를 발생시킨다.")
    void throwException() {
        /* given */

        /* when & then */
        assertThatCode(() -> Token.from(Map.of(ClaimType.MEMBER_ID, "1")))
                .isInstanceOf(JwtInvalidException.class)
                .hasMessage("유효하지 않은 토큰입니다.");
    }
}
