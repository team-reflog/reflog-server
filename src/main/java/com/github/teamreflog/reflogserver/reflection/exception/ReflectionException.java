package com.github.teamreflog.reflogserver.reflection.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class ReflectionException extends RuntimeException {

    private final HttpStatus status;

    protected ReflectionException(final HttpStatus status, final String message) {
        super(message);

        this.status = status;
    }
}
