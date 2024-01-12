package com.github.teamreflog.reflogserver.auth.exception;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class JwtInvalidException extends AuthException {

    private static final String MESSAGE = "유효하지 않은 토큰입니다.";

    public JwtInvalidException() {
        super(UNAUTHORIZED, MESSAGE);
    }

    public JwtInvalidException(final Throwable cause) {
        super(UNAUTHORIZED, MESSAGE, cause);
    }
}
