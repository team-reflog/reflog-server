package com.github.teamreflog.reflogserver.invite.dto;

import com.github.teamreflog.reflogserver.team.domain.Team;

public record InviteQueryResponse(String teamName, String description) {

    public static InviteQueryResponse fromEntity(final Team team) {
        return new InviteQueryResponse(team.getName(), team.getDescription());
    }
}
