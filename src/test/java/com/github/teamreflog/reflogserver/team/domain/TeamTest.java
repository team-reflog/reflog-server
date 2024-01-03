package com.github.teamreflog.reflogserver.team.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.DayOfWeek;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("단위 테스트: Team")
class TeamTest {

    Team team;

    @BeforeEach
    void setUp() {
        team =
                Team.of(
                        "antifragile",
                        "안티프레질 팀입니다.",
                        777L,
                        List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));
    }

    @Test
    @DisplayName("초대 처리 테스트")
    void processInvite() {
        /* given */
        final Invite invite = Invite.of(team, 123L);

        /* when */
        team.processInvite(invite, "super-duper");

        /* then */
        assertAll(
                () ->
                        assertThat(team.getMembers())
                                .extracting(Crew::getNickname)
                                .containsExactly("super-duper"),
                () -> assertThat(team.getInvites()).isEmpty());
    }
}
