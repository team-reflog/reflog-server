package com.github.teamreflog.reflogserver.auth.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class AuthException extends RuntimeException {

    private final HttpStatus status;

    protected AuthException(final HttpStatus status, final String message) {
        super(message);

        this.status = status;
    }

    protected AuthException(final HttpStatus status, final String message, final Throwable cause) {
        super(message, cause);

        this.status = status;
    }
}
