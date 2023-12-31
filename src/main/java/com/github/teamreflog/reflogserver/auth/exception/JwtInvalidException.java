package com.github.teamreflog.reflogserver.auth.exception;

public class JwtInvalidException extends RuntimeException {

    private static final String MESSAGE = "유효하지 않은 토큰입니다.";

    public JwtInvalidException() {
        super(MESSAGE);
    }

    public JwtInvalidException(final Throwable cause) {
        super(MESSAGE, cause);
    }
}
