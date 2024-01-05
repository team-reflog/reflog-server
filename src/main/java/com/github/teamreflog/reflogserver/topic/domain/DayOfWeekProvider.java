package com.github.teamreflog.reflogserver.topic.domain;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.springframework.stereotype.Component;

@Component
public class DayOfWeekProvider {

    public DayOfWeek getToday(final String timezone) {
        return getDayOfWeek(LocalDateTime.now(), timezone);
    }

    DayOfWeek getDayOfWeek(final LocalDateTime now, final String timezone) {
        return ZonedDateTime.of(now, ZoneId.of(timezone)).getDayOfWeek();
    }
}
