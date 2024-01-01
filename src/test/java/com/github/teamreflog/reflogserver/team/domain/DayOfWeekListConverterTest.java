package com.github.teamreflog.reflogserver.team.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.DayOfWeek;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("단위 테스트: DayOfWeekListConverter")
class DayOfWeekListConverterTest {

    DayOfWeekListConverter converter;

    @BeforeEach
    void setUp() {
        converter = new DayOfWeekListConverter();
    }

    @Test
    @DisplayName("요일 목록을 Byte로 변환한다.")
    void convertToByte() {
        /* given */
        final List<DayOfWeek> dayOfWeeks =
                List.of(DayOfWeek.MONDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SUNDAY);

        /* when */
        final Byte result = converter.convertToDatabaseColumn(dayOfWeeks);

        /* then */
        assertThat(result).isEqualTo((byte) 0b1011001);
    }

    @Test
    @DisplayName("Byte를 요일 목록으로 변환한다.")
    void convertToDayOfWeeks() {
        /* given */
        final Byte dayBit = (byte) 0b1011001;

        /* when */
        final List<DayOfWeek> result = converter.convertToEntityAttribute(dayBit);

        /* then */
        assertThat(result)
                .containsExactly(
                        DayOfWeek.MONDAY, DayOfWeek.THURSDAY, DayOfWeek.FRIDAY, DayOfWeek.SUNDAY);
    }

    @Test
    @DisplayName("요일 목록이 비어있으면 0 Byte를 반환한다.")
    void convertToZeroByte() {
        /* given */
        final List<DayOfWeek> dayOfWeeks = List.of();

        /* when */
        final Byte result = converter.convertToDatabaseColumn(dayOfWeeks);

        /* then */
        assertThat(result).isZero();
    }

    @Test
    @DisplayName("0 Byte를 요일 목록으로 변환하면 비어있는 목록을 반환한다.")
    void convertToEmptyDayOfWeeks() {
        /* given */
        final Byte dayBit = 0;

        /* when */
        final List<DayOfWeek> result = converter.convertToEntityAttribute(dayBit);

        /* then */
        assertThat(result).isEmpty();
    }
}
