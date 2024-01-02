package com.github.teamreflog.reflogserver.team.dto;

import com.github.teamreflog.reflogserver.team.domain.TeamInvite;

public record TeamInvitationRequest(String email, Long teamId) {

    public TeamInvite toEntity(Long memberId) {
        return TeamInvite.of(teamId, memberId);
    }
}
