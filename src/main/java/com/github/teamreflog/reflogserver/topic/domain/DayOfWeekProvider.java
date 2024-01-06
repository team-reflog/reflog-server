package com.github.teamreflog.reflogserver.topic.domain;

import java.time.DayOfWeek;
import java.time.Instant;
import java.time.ZoneId;
import org.springframework.stereotype.Component;

@Component
public class DayOfWeekProvider {

    public DayOfWeek getToday(final String timezone) {
        return getDayOfWeek(Instant.now(), timezone);
    }

    DayOfWeek getDayOfWeek(final Instant utc, final String timezone) {
        return utc.atZone(ZoneId.of(timezone)).getDayOfWeek();
    }
}
