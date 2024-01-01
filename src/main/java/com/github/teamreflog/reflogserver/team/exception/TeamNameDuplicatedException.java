package com.github.teamreflog.reflogserver.team.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class TeamNameDuplicatedException extends TeamException {

    public TeamNameDuplicatedException() {
        super(BAD_REQUEST, "이미 사용중인 팀 이름입니다.");
    }
}
