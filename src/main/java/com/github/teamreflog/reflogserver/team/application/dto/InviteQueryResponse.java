package com.github.teamreflog.reflogserver.team.application.dto;

import com.github.teamreflog.reflogserver.team.domain.Invite;

public record InviteQueryResponse(Long id, String teamName, String description) {

    public static InviteQueryResponse fromEntity(final Invite invite) {
        return new InviteQueryResponse(
                invite.getId(), invite.getTeam().getName(), invite.getTeam().getDescription());
    }
}
