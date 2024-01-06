package com.github.teamreflog.reflogserver.team.application.dto;

public record InviteRejectRequest(Long memberId, Long inviteId) {

    public InviteRejectRequest setMemberId(final Long memberId) {
        return new InviteRejectRequest(memberId, this.inviteId);
    }
}
