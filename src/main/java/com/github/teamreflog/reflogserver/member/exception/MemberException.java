package com.github.teamreflog.reflogserver.member.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public abstract class MemberException extends RuntimeException {

    private final HttpStatus status;

    protected MemberException(final HttpStatus status, final String message) {
        super(message);

        this.status = status;
    }
}
