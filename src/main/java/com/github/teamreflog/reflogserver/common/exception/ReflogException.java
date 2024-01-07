package com.github.teamreflog.reflogserver.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class ReflogException extends RuntimeException {

    private final HttpStatus status;

    protected ReflogException(final HttpStatus status, final String message) {
        super(message);

        this.status = status;
    }

    protected ReflogException(
            final HttpStatus status, final String message, final Throwable cause) {
        super(message, cause);

        this.status = status;
    }
}
