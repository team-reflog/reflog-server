package com.github.teamreflog.reflogserver.member.dto;

import com.github.teamreflog.reflogserver.member.domain.Member;
import com.github.teamreflog.reflogserver.member.domain.MemberEmail;

public record MemberJoinRequest(String email, String password) {

    public Member toEntity() {
        return Member.of(email, password);
    }

    public MemberEmail getMemberEmail() {
        return new MemberEmail(email);
    }
}
