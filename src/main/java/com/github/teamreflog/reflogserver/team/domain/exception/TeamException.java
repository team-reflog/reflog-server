package com.github.teamreflog.reflogserver.team.domain.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class TeamException extends RuntimeException {

    private final HttpStatus status;

    protected TeamException(final HttpStatus status, final String message) {
        super(message);

        this.status = status;
    }
}
