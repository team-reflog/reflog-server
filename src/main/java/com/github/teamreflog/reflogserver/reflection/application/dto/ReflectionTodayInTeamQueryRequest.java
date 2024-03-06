package com.github.teamreflog.reflogserver.reflection.application.dto;

import com.github.teamreflog.reflogserver.reflection.domain.TeamData;
import java.util.List;

public record ReflectionTodayInTeamQueryRequest(
        Long teamId, String teamName, List<ReflectionDetailResponse> reflections) {

    public static ReflectionTodayInTeamQueryRequest fromEntity(
            final TeamData team, final List<ReflectionDetailResponse> reflections) {
        return new ReflectionTodayInTeamQueryRequest(team.teamId(), team.teamName(), reflections);
    }
}
