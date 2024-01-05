package com.github.teamreflog.reflogserver.reflection.domain;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.springframework.stereotype.Component;

@Component
public class DateProvider {

    public DateRange getTodayRange(final String timezone) {
        return getTodayRange(timezone, LocalDateTime.now());
    }

    DateRange getTodayRange(final String timezone, final LocalDateTime now) {
        final ZonedDateTime zonedDateTime = ZonedDateTime.of(now, ZoneId.of(timezone));

        final LocalDateTime start =
                ZonedDateTime.of(
                                zonedDateTime.getYear(),
                                zonedDateTime.getMonthValue(),
                                zonedDateTime.getDayOfMonth(),
                                0,
                                0,
                                0,
                                0,
                                ZoneId.of(timezone))
                        .toLocalDateTime();
        final LocalDateTime end =
                ZonedDateTime.of(
                                zonedDateTime.getYear(),
                                zonedDateTime.getMonthValue(),
                                zonedDateTime.getDayOfMonth(),
                                23,
                                59,
                                59,
                                999999999,
                                ZoneId.of(timezone))
                        .toLocalDateTime();

        return new DateRange(start, end);
    }
}
