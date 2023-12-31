package com.github.teamreflog.reflogserver.auth.dto;

import com.github.teamreflog.reflogserver.member.domain.MemberEmail;

public record LoginRequest(String email, String password) {

    public MemberEmail getMemberEmail() {
        return new MemberEmail(email);
    }
}
