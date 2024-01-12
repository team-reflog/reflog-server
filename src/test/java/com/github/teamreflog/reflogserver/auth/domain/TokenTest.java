package com.github.teamreflog.reflogserver.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("단위 테스트: Token")
class TokenTest {

    @Test
    @DisplayName("토큰에서 클레임을 추출한다.")
    void getClaim() {
        /* given */
        final Map<ClaimType, String> claims =
                Map.of(
                        ClaimType.MEMBER_ID, "1",
                        ClaimType.ROLE, "MEMBER");
        final Token token = new Token(claims);

        /* when & then */
        assertThat(token.getSubject()).isEqualTo(1L);
    }
}
