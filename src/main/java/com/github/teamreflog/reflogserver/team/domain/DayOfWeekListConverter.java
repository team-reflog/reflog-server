package com.github.teamreflog.reflogserver.team.domain;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.time.DayOfWeek;
import java.util.List;
import java.util.stream.Stream;

/**
 * 뒤에서부터 월화수목금토일을 나타낸다.
 *
 * <p>e.g. 0b1011001 -> 월, 목, 금, 일
 */
@Converter
public class DayOfWeekListConverter implements AttributeConverter<List<DayOfWeek>, Byte> {

    @Override
    public Byte convertToDatabaseColumn(final List<DayOfWeek> dayOfWeeks) {
        return (byte)
                dayOfWeeks.stream()
                        .mapToInt(DayOfWeek::getValue)
                        .map(value -> 1 << (value - 1))
                        .sum();
    }

    @Override
    public List<DayOfWeek> convertToEntityAttribute(final Byte dayBit) {
        return Stream.of(DayOfWeek.values())
                .filter(dayOfWeek -> (dayBit & (1 << dayOfWeek.getValue() - 1)) != 0)
                .toList();
    }
}
