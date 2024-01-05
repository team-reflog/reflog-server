package com.github.teamreflog.reflogserver.topic.domain;

import java.time.DayOfWeek;
import java.util.List;

public record TeamData(Long id, Long ownerId, List<DayOfWeek> reflectionDays) {

    public boolean isOwner(final Long memberId) {
        return ownerId.equals(memberId);
    }

    public boolean containsReflectionDay(final DayOfWeek today) {
        return reflectionDays.contains(today);
    }
}
