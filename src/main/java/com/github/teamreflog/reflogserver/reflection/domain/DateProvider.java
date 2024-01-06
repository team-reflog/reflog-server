package com.github.teamreflog.reflogserver.reflection.domain;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import org.springframework.stereotype.Component;

@Component
public class DateProvider {

    public LocalDate getTodayOfZone(final String timezone) {
        return getTodayOfZone(Instant.now(), timezone);
    }

    LocalDate getTodayOfZone(final Instant utc, final String timezone) {
        return utc.atZone(ZoneId.of(timezone)).toLocalDate();
    }
}
