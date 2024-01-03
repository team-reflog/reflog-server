package com.github.teamreflog.reflogserver.topic.domain.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class TopicException extends RuntimeException {

    private final HttpStatus status;

    protected TopicException(final HttpStatus status, final String message) {
        super(message);

        this.status = status;
    }
}
