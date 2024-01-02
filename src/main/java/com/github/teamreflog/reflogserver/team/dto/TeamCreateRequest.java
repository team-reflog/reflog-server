package com.github.teamreflog.reflogserver.team.dto;

import com.github.teamreflog.reflogserver.team.domain.Team;
import java.time.DayOfWeek;
import java.util.List;

public record TeamCreateRequest(
        String name, String description, String nickname, List<DayOfWeek> reflectionDays) {

    public Team toEntity(final Long ownerId) {
        return Team.of(name, description, ownerId, reflectionDays);
    }
}
