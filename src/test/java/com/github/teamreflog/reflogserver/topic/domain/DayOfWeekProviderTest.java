package com.github.teamreflog.reflogserver.topic.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
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

    // TODO: 미국 시간으로 테스트해보기
    @Test
    @DisplayName("현재 시간을 Timezone에 맞게 반환한다.")
    void getDayOfWeek() {
        /* given */
        final LocalDateTime now = LocalDateTime.now();

        /* when */
        final DayOfWeek dayOfWeek = dayOfWeekProvider.getDayOfWeek(now, "Asia/Seoul");

        /* then */
        assertThat(dayOfWeek).isEqualTo(now.getDayOfWeek());
    }
}
