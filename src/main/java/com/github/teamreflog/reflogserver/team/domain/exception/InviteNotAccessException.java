package com.github.teamreflog.reflogserver.team.domain.exception;

import org.springframework.http.HttpStatus;

public class InviteNotAccessException extends TeamException {

    public InviteNotAccessException() {
        super(HttpStatus.UNAUTHORIZED, "팀장만 초대할 수 있습니다.");
    }
}
