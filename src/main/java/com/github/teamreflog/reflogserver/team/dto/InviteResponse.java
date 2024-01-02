package com.github.teamreflog.reflogserver.team.dto;

import com.github.teamreflog.reflogserver.team.domain.Team;

public record InviteResponse(String teamName, String description) {

    public static InviteResponse fromEntity(final Team team) {
        return new InviteResponse(team.getName(), team.getDescription());
    }
}
