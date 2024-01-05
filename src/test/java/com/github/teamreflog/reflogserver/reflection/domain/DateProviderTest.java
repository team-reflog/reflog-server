package com.github.teamreflog.reflogserver.reflection.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.LocalDateTime;
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

    // TODO: 미국 시간으로 테스트 해보기
    @Test
    @DisplayName("Timezone이 주어지면 오늘의 범위를 반환한다.")
    void getTodayRangeTest() {
        /* given */
        final String timezone = "Asia/Seoul";
        final LocalDateTime now = LocalDateTime.of(2024, 1, 5, 18, 0, 0, 0);

        /* when */
        final DateRange todayRange = dateProvider.getTodayRange(timezone, now);

        /* then */
        final LocalDateTime start = LocalDateTime.of(2024, 1, 5, 0, 0, 0, 0);
        final LocalDateTime end = LocalDateTime.of(2024, 1, 5, 23, 59, 59, 999999999);
        assertAll(
                () -> assertThat(todayRange.start()).isEqualTo(start),
                () -> assertThat(todayRange.end()).isEqualTo(end));
    }
}
