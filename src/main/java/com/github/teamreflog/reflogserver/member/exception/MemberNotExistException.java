package com.github.teamreflog.reflogserver.member.exception;

import org.springframework.http.HttpStatus;

public class MemberNotExistException extends MemberException {

    public MemberNotExistException() {
        super(HttpStatus.BAD_REQUEST, "존재하지 않는 사용자입니다.");
    }
}
