package com.github.teamreflog.reflogserver.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("단위 테스트: Jwt")
class JwtTest {

    @Test
    @DisplayName("토큰에서 클레임을 추출한다.")
    void getClaim() {
        /* given */
        final Map<String, Object> claims =
                Map.of(
                        "sub", "1",
                        "role", "MEMBER");
        final Jwt jwt = new Jwt(claims);

        /* when */
        final String subject = jwt.getClaim(ClaimType.MEMBER_ID);
        final String role = jwt.getClaim(ClaimType.ROLE);

        /* then */
        assertAll(
                () -> assertThat(subject).isEqualTo("1"),
                () -> assertThat(role).isEqualTo("MEMBER"));
    }
}
