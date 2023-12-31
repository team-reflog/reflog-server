package com.github.teamreflog.reflogserver.auth.exception;

public class JwtInvaildException extends RuntimeException {

    public JwtInvaildException(final Throwable cause) {
        super("유효하지 않은 토큰입니다.", cause);
    }
}
