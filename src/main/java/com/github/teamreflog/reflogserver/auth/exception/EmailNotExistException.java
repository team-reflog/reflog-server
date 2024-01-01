package com.github.teamreflog.reflogserver.auth.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class EmailNotExistException extends AuthException {

    private static final String MESSAGE = "이메일이 존재하지 않습니다.";

    public EmailNotExistException() {
        super(BAD_REQUEST, MESSAGE);
    }
}
