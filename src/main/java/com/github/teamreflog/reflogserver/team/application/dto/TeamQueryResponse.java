package com.github.teamreflog.reflogserver.team.application.dto;

import com.github.teamreflog.reflogserver.team.domain.Team;
import java.time.DayOfWeek;
import java.util.List;

public record TeamQueryResponse(
        Long id, String name, String description, Long ownerId, List<DayOfWeek> reflectionDays) {

    public static TeamQueryResponse fromEntity(final Team team) {
        return new TeamQueryResponse(
                team.getId(),
                team.getName(),
                team.getDescription(),
                team.getOwnerId(),
                team.getReflectionDays());
    }
}
