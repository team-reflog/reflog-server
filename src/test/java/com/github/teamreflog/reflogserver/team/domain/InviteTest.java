package com.github.teamreflog.reflogserver.team.domain;

import static org.assertj.core.api.Assertions.assertThatCode;

import com.github.teamreflog.reflogserver.team.domain.exception.UnauthorizedInviteException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("단위 테스트: Invite")
class InviteTest {

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
