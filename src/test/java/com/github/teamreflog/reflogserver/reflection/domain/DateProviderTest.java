package com.github.teamreflog.reflogserver.reflection.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("단위 테스트: DateProvider")
class DateProviderTest {

    @Test
    @DisplayName("Timezone이 주어지면 오늘의 범위를 반환한다.")
    void getTodayRangeTest() {
        /* given */
        final String timezone = "Asia/Seoul";
        final ZonedDateTime today = ZonedDateTime.of(2024, 1, 5, 18, 0, 0, 0, ZoneId.of(timezone));
        final DateProvider dateProvider = new DateProvider(today::toLocalDateTime);

        /* when */
        final DateRange todayRange = dateProvider.getTodayRange(timezone);

        /* then */
        final LocalDateTime start =
                ZonedDateTime.of(2024, 1, 5, 0, 0, 0, 0, ZoneId.of(timezone)).toLocalDateTime();
        final LocalDateTime end =
                ZonedDateTime.of(2024, 1, 5, 23, 59, 59, 999999999, ZoneId.of(timezone))
                        .toLocalDateTime();
        assertAll(
                () -> assertThat(todayRange.start()).isEqualTo(start),
                () -> assertThat(todayRange.end()).isEqualTo(end));
    }
}
