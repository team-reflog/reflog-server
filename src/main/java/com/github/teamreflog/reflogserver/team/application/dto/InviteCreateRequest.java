package com.github.teamreflog.reflogserver.team.application.dto;

public record InviteCreateRequest(Long memberId, String email, Long teamId) {

    public InviteCreateRequest setMemberId(final Long memberId) {
        return new InviteCreateRequest(memberId, this.email, this.teamId);
    }
}
