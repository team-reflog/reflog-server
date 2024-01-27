package com.github.teamreflog.reflogserver.reflection.domain;

import java.time.DayOfWeek;
import java.util.List;

public record TeamData(List<DayOfWeek> reflectionDays) {

    public boolean containsReflectionDay(final DayOfWeek dayOfWeek) {
        return reflectionDays.contains(dayOfWeek);
    }
}
