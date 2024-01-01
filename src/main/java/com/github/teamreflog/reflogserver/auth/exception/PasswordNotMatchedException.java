package com.github.teamreflog.reflogserver.auth.exception;

import org.springframework.http.HttpStatus;

public class PasswordNotMatchedException extends AuthException {

    private static final String MESSAGE = "비밀번호가 일치하지 않습니다.";

    public PasswordNotMatchedException() {
        super(HttpStatus.BAD_REQUEST, MESSAGE);
    }
}
