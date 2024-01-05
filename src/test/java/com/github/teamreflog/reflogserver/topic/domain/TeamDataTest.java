package com.github.teamreflog.reflogserver.topic.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.DayOfWeek;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("단위 테스트: TeamData")
class TeamDataTest {

    @Test
    @DisplayName("팀장인지 확인할 수 있다.")
    void isOwnerTest() {
        /* given */
        final TeamData data =
                new TeamData(
                        1L, 1L, List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.THURSDAY));

        /* when & then */
        assertAll(
                () -> assertThat(data.isOwner(1L)).isTrue(),
                () -> assertThat(data.isOwner(2L)).isFalse());
    }

    @Test
    @DisplayName("회고 요일을 포함하고 있는지 확인할 수 있다.")
    void containsReflectionDayTest() {
        /* given */
        final TeamData data =
                new TeamData(
                        1L, 1L, List.of(DayOfWeek.MONDAY, DayOfWeek.TUESDAY, DayOfWeek.THURSDAY));

        /* when & then */
        assertAll(
                () -> assertThat(data.containsReflectionDay(DayOfWeek.MONDAY)).isTrue(),
                () -> assertThat(data.containsReflectionDay(DayOfWeek.WEDNESDAY)).isFalse());
    }
}
