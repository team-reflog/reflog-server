package com.github.teamreflog.reflogserver.invite.dto;

import com.github.teamreflog.reflogserver.invite.domain.Invite;

public record InviteCreateRequest(String email, Long teamId) {

    public Invite toEntity(final Long memberId) {
        return Invite.of(teamId, memberId);
    }
}
