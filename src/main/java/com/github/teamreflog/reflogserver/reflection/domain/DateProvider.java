package com.github.teamreflog.reflogserver.reflection.domain;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.springframework.stereotype.Component;

@Component
public class DateProvider {

    public DateRange getTodayRange(final String timezone) {
        return getTodayRange(Instant.now(), timezone);
    }

    DateRange getTodayRange(final Instant utc, final String timezone) {
        final ZonedDateTime zonedDateTime = utc.atZone(ZoneId.of(timezone));

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
