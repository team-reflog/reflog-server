package com.github.teamreflog.reflogserver.member.dto;

import com.github.teamreflog.reflogserver.member.domain.Member;

public record MemberJoinRequest(String email, String password) {

    public Member toEntity() {
        return Member.of(email, password);
    }
}
