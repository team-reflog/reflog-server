package com.github.teamreflog.reflogserver.invite.exception;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class UnauthorizedInviteException extends InviteException {

    public UnauthorizedInviteException() {
        super(UNAUTHORIZED, "초대 수락 권한이 없습니다.");
    }
}
