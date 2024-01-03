package com.github.teamreflog.reflogserver.team.domain.exception;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class UnauthorizedInviteException extends TeamException {

    public UnauthorizedInviteException() {
        super(UNAUTHORIZED, "초대 수락 권한이 없습니다.");
    }
}
