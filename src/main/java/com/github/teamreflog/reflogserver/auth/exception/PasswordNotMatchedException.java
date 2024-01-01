package com.github.teamreflog.reflogserver.auth.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class PasswordNotMatchedException extends AuthException {

    private static final String MESSAGE = "비밀번호가 일치하지 않습니다.";

    public PasswordNotMatchedException() {
        super(BAD_REQUEST, MESSAGE);
    }
}
