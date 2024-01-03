package com.github.teamreflog.reflogserver.team.domain.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class MemberAlreadyInvitedException extends TeamException {

    public MemberAlreadyInvitedException() {
        super(BAD_REQUEST, "이미 초대된 사용자입니다.");
    }
}
