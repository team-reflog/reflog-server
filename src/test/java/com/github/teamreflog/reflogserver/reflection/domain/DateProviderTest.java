package com.github.teamreflog.reflogserver.reflection.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("단위 테스트: DateProvider")
class DateProviderTest {

    DateProvider dateProvider;

    @BeforeEach
    void setUp() {
        dateProvider = new DateProvider();
    }

    @Test
    @DisplayName("Timezone이 주어지면 오늘의 범위를 반환한다.")
    void getTodayRangeTest() {
        /* given */
        final Instant now = ZonedDateTime.of(2024, 1, 5, 4, 0, 0, 0, ZoneOffset.UTC).toInstant();

        /* when */
        final DateRange seoulTodayRange = dateProvider.getTodayRange(now, "Asia/Seoul");
        final DateRange newYorkTodayRange = dateProvider.getTodayRange(now, "America/New_York");

        /* then */
        final LocalDateTime seoulStart =
                ZonedDateTime.of(2024, 1, 5, 0, 0, 0, 0, ZoneId.of("Asia/Seoul")).toLocalDateTime();
        final LocalDateTime seoulEnd =
                ZonedDateTime.of(2024, 1, 5, 23, 59, 59, 999999999, ZoneId.of("Asia/Seoul"))
                        .toLocalDateTime();
        final LocalDateTime newYorkStart =
                ZonedDateTime.of(2024, 1, 4, 0, 0, 0, 0, ZoneId.of("America/New_York"))
                        .toLocalDateTime();
        final LocalDateTime newYorkEnd =
                ZonedDateTime.of(2024, 1, 4, 23, 59, 59, 999999999, ZoneId.of("America/New_York"))
                        .toLocalDateTime();

        assertAll(
                () -> assertThat(seoulTodayRange.start()).isEqualTo(seoulStart),
                () -> assertThat(seoulTodayRange.end()).isEqualTo(seoulEnd),
                () -> assertThat(newYorkTodayRange.start()).isEqualTo(newYorkStart),
                () -> assertThat(newYorkTodayRange.end()).isEqualTo(newYorkEnd));
    }
}
