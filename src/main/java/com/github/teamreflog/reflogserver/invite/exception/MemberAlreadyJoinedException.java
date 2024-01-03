package com.github.teamreflog.reflogserver.invite.exception;

import com.github.teamreflog.reflogserver.team.exception.TeamException;
import org.springframework.http.HttpStatus;

public class MemberAlreadyJoinedException extends TeamException {

    public MemberAlreadyJoinedException() {
        super(HttpStatus.BAD_REQUEST, "이미 팀 멤버에 존재합니다.");
    }
}
