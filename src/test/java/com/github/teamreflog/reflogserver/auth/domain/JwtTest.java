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
        final Map<ClaimType, String> claims =
                Map.of(
                        ClaimType.MEMBER_ID, "1",
                        ClaimType.ROLE, "MEMBER");
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
