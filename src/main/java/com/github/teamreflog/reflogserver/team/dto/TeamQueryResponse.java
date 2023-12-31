package com.github.teamreflog.reflogserver.team.dto;

import com.github.teamreflog.reflogserver.team.domain.Team;
import java.time.DayOfWeek;
import java.util.List;

public record TeamQueryResponse(
        String name, String description, Long ownerId, List<DayOfWeek> daysOfWeek) {

    public static TeamQueryResponse fromEntity(final Team team) {
        return new TeamQueryResponse(
                team.getName(), team.getDescription(), team.getOwnerId(), team.getReflectionDays());
    }
}
