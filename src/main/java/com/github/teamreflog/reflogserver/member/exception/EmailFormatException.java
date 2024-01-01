package com.github.teamreflog.reflogserver.member.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class EmailFormatException extends MemberException {

    public EmailFormatException() {
        super(BAD_REQUEST, "이메일 형식이 올바르지 않습니다.");
    }
}
