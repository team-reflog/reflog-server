package com.github.teamreflog.reflogserver.auth.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

@DisplayName("단위 테스트: MemberPasswordEncoderImpl")
class MemberPasswordEncoderImplTest {

    MemberPasswordEncoderImpl memberPasswordEncoder;
    PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        passwordEncoder = mock(PasswordEncoder.class);
        memberPasswordEncoder = new MemberPasswordEncoderImpl(passwordEncoder);
    }

    @Nested
    @DisplayName("raw 비밀번호와 encoded 비밀번호가 일치하는지 확인할 때")
    class WhenMatches {

        @Test
        @DisplayName("일치하면 true를 반환한다")
        void returnTrueIfMatches() {
            /* given */
            final String rawPassword = "correctRaw";
            final String encodedPassword = "encoded";
            given(passwordEncoder.matches(rawPassword, encodedPassword)).willReturn(true);

            /* when & then  */
            assertThat(memberPasswordEncoder.matches(rawPassword, encodedPassword)).isTrue();
        }

        @Test
        @DisplayName("일치하지 않으면 false를 반환한다")
        void returnFalseIfNotMatches() {
            /* given */
            final String rawPassword = "wrongRaw";
            final String encodedPassword = "encoded";
            given(passwordEncoder.matches(rawPassword, encodedPassword)).willReturn(false);

            /* when & then  */
            assertThat(memberPasswordEncoder.matches(rawPassword, encodedPassword)).isFalse();
        }
    }

    @Nested
    @DisplayName("비밀번호를 인코딩할 때")
    class WhenEncode {

        @Test
        @DisplayName("인코딩된 비밀번호를 반환한다")
        void returnEncodedPassword() {
            /* given */
            final String rawPassword = "raw";
            final String encodedPassword = "encoded";
            given(passwordEncoder.encode(rawPassword)).willReturn(encodedPassword);

            /* when & then  */
            assertThat(memberPasswordEncoder.encode(rawPassword)).isEqualTo(encodedPassword);
        }
    }
}
