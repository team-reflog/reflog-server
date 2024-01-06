package com.github.teamreflog.reflogserver.topic.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("단위 테스트: DayOfWeekProvider")
class DayOfWeekProviderTest {

    DayOfWeekProvider dayOfWeekProvider;

    @BeforeEach
    void setUp() {
        dayOfWeekProvider = new DayOfWeekProvider();
    }

    @Test
    @DisplayName("현재 시간을 Timezone에 맞게 반환한다.")
    void getDayOfWeek() {
        /* given */
        final Instant now = ZonedDateTime.of(2024, 1, 5, 4, 0, 0, 0, ZoneOffset.UTC).toInstant();

        /* when */
        final DayOfWeek seoulDayOfWeek = dayOfWeekProvider.getDayOfWeek(now, "Asia/Seoul");
        final DayOfWeek newYorkDayOfWeek = dayOfWeekProvider.getDayOfWeek(now, "America/New_York");

        /* then */
        assertAll(
                () -> assertThat(seoulDayOfWeek).isEqualTo(DayOfWeek.FRIDAY),
                () -> assertThat(newYorkDayOfWeek).isEqualTo(DayOfWeek.THURSDAY));
    }
}
