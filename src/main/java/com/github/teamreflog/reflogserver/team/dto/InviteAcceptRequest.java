package com.github.teamreflog.reflogserver.team.dto;

import com.github.teamreflog.reflogserver.team.domain.TeamMember;

public record InviteAcceptRequest(Long teamId, String nickname) {

    public TeamMember toEntity(final Long memberId) {
        return TeamMember.of(this.teamId, memberId, this.nickname);
    }
}
