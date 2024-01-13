package com.github.teamreflog.reflogserver.team.domain.exception;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class InviteNotAccessException extends TeamException {

    public InviteNotAccessException() {
        super(UNAUTHORIZED, "초대할 권한이 없습니다.");
    }
}
