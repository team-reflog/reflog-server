package com.github.teamreflog.reflogserver.team.application.dto;

import com.github.teamreflog.reflogserver.team.domain.Team;
import java.time.DayOfWeek;
import java.util.List;

public record TeamCreateRequest(
        Long ownerId,
        String name,
        String description,
        String nickname,
        List<DayOfWeek> reflectionDays) {

    public Team toEntity() {
        return Team.of(name, description, ownerId, reflectionDays);
    }

    public TeamCreateRequest setMemberId(final Long memberId) {
        return new TeamCreateRequest(
                memberId, this.name, this.description, this.nickname, this.reflectionDays);
    }
}
