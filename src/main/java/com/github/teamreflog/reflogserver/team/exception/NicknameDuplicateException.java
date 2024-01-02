package com.github.teamreflog.reflogserver.team.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class NicknameDuplicateException extends TeamException {

    public NicknameDuplicateException() {
        super(BAD_REQUEST, "팀 내에 이미 같은 이름이 있습니다.");
    }
}
