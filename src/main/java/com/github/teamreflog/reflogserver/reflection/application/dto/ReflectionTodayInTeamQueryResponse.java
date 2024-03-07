package com.github.teamreflog.reflogserver.reflection.application.dto;

import com.github.teamreflog.reflogserver.reflection.domain.TeamData;
import java.util.List;

public record ReflectionTodayInTeamQueryResponse(
        Long teamId, String teamName, List<ReflectionDetailResponse> reflections) {

    public static ReflectionTodayInTeamQueryResponse fromEntity(
            final TeamData team, final List<ReflectionDetailResponse> reflections) {
        return new ReflectionTodayInTeamQueryResponse(team.teamId(), team.teamName(), reflections);
    }
}
