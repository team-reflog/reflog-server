package com.github.teamreflog.reflogserver.team.domain.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class InviteNotExistException extends TeamException {

    public InviteNotExistException() {
        super(BAD_REQUEST, "초대가 존재하지 않습니다.");
    }
}
