package com.github.teamreflog.reflogserver.reflection.domain;

import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import org.springframework.stereotype.Component;

@Component
public class DateProvider {

    public LocalDate getLocalDateNow(final String timezone) {
        return getLocalDateNow(Instant.now(), timezone);
    }

    LocalDate getLocalDateNow(final Instant utc, final String timezone) {
        return utc.atZone(ZoneId.of(timezone)).toLocalDate();
    }
}
