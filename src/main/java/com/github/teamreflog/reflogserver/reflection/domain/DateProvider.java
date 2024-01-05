package com.github.teamreflog.reflogserver.reflection.domain;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class DateProvider {

    private static final NowGenerator DEFAULT_NOW_GENERATOR = LocalDateTime::now;

    private final NowGenerator nowGenerator;

    public DateProvider() {
        this(DEFAULT_NOW_GENERATOR);
    }

    public DateRange getTodayRange(final String timezone) {
        final ZonedDateTime now = ZonedDateTime.of(nowGenerator.getNow(), ZoneId.of(timezone));

        final LocalDateTime start =
                ZonedDateTime.of(
                                now.getYear(),
                                now.getMonthValue(),
                                now.getDayOfMonth(),
                                0,
                                0,
                                0,
                                0,
                                ZoneId.of(timezone))
                        .toLocalDateTime();
        final LocalDateTime end =
                ZonedDateTime.of(
                                now.getYear(),
                                now.getMonthValue(),
                                now.getDayOfMonth(),
                                23,
                                59,
                                59,
                                999999999,
                                ZoneId.of(timezone))
                        .toLocalDateTime();

        return new DateRange(start, end);
    }

    public interface NowGenerator {

        LocalDateTime getNow();
    }
}
