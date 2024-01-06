package com.github.teamreflog.reflogserver.team.domain;

import static org.assertj.core.api.Assertions.assertThatCode;

import com.github.teamreflog.reflogserver.team.domain.exception.UnauthorizedInviteException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("단위 테스트: Invite")
class InviteTest {

    @Nested
    @DisplayName("초대를 수락할 때")
    class AcceptInviteTest {

        @Test
        @DisplayName("초대 대상 사용자와 요청 사용자가 일치하는 경우 예외가 발생하지 않는다.")
        void userValid() {
            /* given */
            final Invite invite = Invite.of(1L, 1L);

            /* when & then */
            assertThatCode(() -> invite.accept(1L)).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("초대 대상 사용자와 요청 사용자가 일치하지 않는 경우 예외가 발생한다.")
        void userInvalidThrowsException() {
            /* given */
            final Invite invite = Invite.of(1L, 1L);

            /* when & then */
            assertThatCode(() -> invite.accept(2L))
                    .isExactlyInstanceOf(UnauthorizedInviteException.class);
        }
    }

    @Nested
    @DisplayName("초대를 거절할 때")
    class RejectInviteTest {

        @Test
        @DisplayName("초대 대상 사용자와 요청 사용자가 일치하는 경우 예외가 발생하지 않는다.")
        void userValid() {
            /* given */
            final Invite invite = Invite.of(1L, 1L);

            /* when & then */
            assertThatCode(() -> invite.reject(1L)).doesNotThrowAnyException();
        }

        @Test
        @DisplayName("초대 대상 사용자와 요청 사용자가 일치하지 않는 경우 예외가 발생한다.")
        void userInvalidThrowsException() {
            /* given */
            final Invite invite = Invite.of(1L, 1L);

            /* when & then */
            assertThatCode(() -> invite.reject(2L))
                    .isExactlyInstanceOf(UnauthorizedInviteException.class);
        }
    }
}
