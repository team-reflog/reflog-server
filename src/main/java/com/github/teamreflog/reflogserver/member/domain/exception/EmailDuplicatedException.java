package com.github.teamreflog.reflogserver.member.domain.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class EmailDuplicatedException extends MemberException {

    public EmailDuplicatedException() {
        super(BAD_REQUEST, "이미 사용중인 이메일입니다.");
    }
}
