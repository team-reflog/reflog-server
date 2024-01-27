package com.github.teamreflog.reflogserver.reflection.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import java.time.Instant;
import java.time.LocalDate;
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
    @DisplayName("Timezone이 주어지면 현재 시간을 반환한다.")
    void getLocalDateNow() {
        /* given */
        final Instant now = ZonedDateTime.of(2024, 1, 5, 4, 0, 0, 0, ZoneOffset.UTC).toInstant();

        /* when */
        final LocalDate seoulToday = dateProvider.getLocalDateNow(now, "Asia/Seoul");
        final LocalDate newYorkToday = dateProvider.getLocalDateNow(now, "America/New_York");

        /* then */
        assertAll(
                () -> assertThat(seoulToday).isEqualTo(LocalDate.of(2024, 1, 5)),
                () -> assertThat(newYorkToday).isEqualTo(LocalDate.of(2024, 1, 4)));
    }
}
