package com.github.teamreflog.reflogserver.auth.exception;

import org.springframework.http.HttpStatus;

public class JwtInvalidException extends AuthException {

    private static final String MESSAGE = "유효하지 않은 토큰입니다.";

    public JwtInvalidException() {
        super(HttpStatus.BAD_REQUEST, MESSAGE);
    }

    public JwtInvalidException(final Throwable cause) {
        super(HttpStatus.BAD_REQUEST, MESSAGE, cause);
    }
}
