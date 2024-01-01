package com.github.teamreflog.reflogserver.auth.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class JwtInvalidException extends AuthException {

    private static final String MESSAGE = "유효하지 않은 토큰입니다.";

    public JwtInvalidException() {
        super(BAD_REQUEST, MESSAGE);
    }

    public JwtInvalidException(final Throwable cause) {
        super(BAD_REQUEST, MESSAGE, cause);
    }
}
