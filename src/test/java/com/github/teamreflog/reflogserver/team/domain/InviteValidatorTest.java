package com.github.teamreflog.reflogserver.team.domain;

import static org.assertj.core.api.Assertions.assertThatCode;

import com.github.teamreflog.reflogserver.topic.domain.exception.NotOwnerException;
import java.time.DayOfWeek;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("단위 테스트: InviteValidator")
class InviteValidatorTest {

    InviteValidator inviteValidator;

    @BeforeEach
    void setUp() {
        this.inviteValidator = new InviteValidator(null);
    }

    @Test
    @DisplayName("팀장이 아닌 경우 예외가 발생한다.")
    void throwExceptionNotOwner() {
        /* given */
        Long notOwnerMemberId = 777L;
        Team team = Team.of("anti-fragile", "anti", 1L, List.of(DayOfWeek.FRIDAY));

        /* when & then */
        assertThatCode(() -> inviteValidator.validateTeamOwnerAuthorization(notOwnerMemberId, team))
                .isExactlyInstanceOf(NotOwnerException.class);
    }
}
