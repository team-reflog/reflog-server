package com.github.teamreflog.reflogserver.team.application.dto;

import com.github.teamreflog.reflogserver.team.domain.Team;
import java.util.List;

public record TeamReflectionQueryResponse(
        Long id, String name, List<TeamReflectionDetailResponse> reflections) {

    public static TeamReflectionQueryResponse fromEntity(
            final Team team, final List<TeamReflectionDetailResponse> reflections) {

        return new TeamReflectionQueryResponse(team.getId(), team.getName(), reflections);
    }
}
