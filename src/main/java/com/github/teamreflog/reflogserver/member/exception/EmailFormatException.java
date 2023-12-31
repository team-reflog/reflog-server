package com.github.teamreflog.reflogserver.member.exception;

public class EmailFormatException extends RuntimeException {

    public EmailFormatException() {
        super("이메일 형식이 올바르지 않습니다.");
    }
}
