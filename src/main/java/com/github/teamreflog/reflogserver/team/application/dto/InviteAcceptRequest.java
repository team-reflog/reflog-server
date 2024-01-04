package com.github.teamreflog.reflogserver.team.application.dto;

public record InviteAcceptRequest(Long memberId, Long inviteId, String nickname) {

    public InviteAcceptRequest setMemberId(final Long memberId) {
        return new InviteAcceptRequest(memberId, this.inviteId, this.nickname);
    }

    public InviteAcceptRequest setInviteId(final Long inviteId) {
        return new InviteAcceptRequest(this.memberId, inviteId, this.nickname);
    }
}
