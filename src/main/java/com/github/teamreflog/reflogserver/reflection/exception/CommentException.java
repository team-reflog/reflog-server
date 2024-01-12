package com.github.teamreflog.reflogserver.reflection.exception;

import org.springframework.http.HttpStatus;

public abstract class CommentException extends RuntimeException {

    private final HttpStatus status;

    protected CommentException(
            final HttpStatus status, final String message, final Throwable cause) {
        super(message, cause);

        this.status = status;
    }
}
