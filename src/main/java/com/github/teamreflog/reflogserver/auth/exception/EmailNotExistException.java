package com.github.teamreflog.reflogserver.auth.exception;

public class EmailNotExistException extends RuntimeException {

    public EmailNotExistException() {
        super("이메일이 존재하지 않습니다.");
    }
}
