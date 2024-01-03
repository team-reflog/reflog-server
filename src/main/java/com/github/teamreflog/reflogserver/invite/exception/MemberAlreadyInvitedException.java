package com.github.teamreflog.reflogserver.invite.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

import com.github.teamreflog.reflogserver.team.exception.TeamException;

public class MemberAlreadyInvitedException extends TeamException {

    public MemberAlreadyInvitedException() {
        super(BAD_REQUEST, "이미 초대된 사용자입니다.");
    }
}
