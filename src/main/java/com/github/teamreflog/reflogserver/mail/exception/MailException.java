package com.github.teamreflog.reflogserver.mail.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class MailException extends RuntimeException {

    private final HttpStatus status;

    protected MailException(final HttpStatus status, final String message) {
        super(message);

        this.status = status;
    }
}
