package com.github.teamreflog.reflogserver.invite.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class InviteException extends RuntimeException {

    private final HttpStatus status;

    protected InviteException(final HttpStatus status, final String message) {
        super(message);

        this.status = status;
    }
}
