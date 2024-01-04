package com.github.teamreflog.reflogserver.team.application.dto;

import com.github.teamreflog.reflogserver.team.domain.Invite;
import com.github.teamreflog.reflogserver.team.domain.Team;

public record InviteQueryResponse(Long id, String teamName, String description) {

    public static InviteQueryResponse fromEntity(final Invite invite, final Team team) {
        return new InviteQueryResponse(invite.getId(), team.getName(), team.getDescription());
    }
}
