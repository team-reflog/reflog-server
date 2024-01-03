package com.github.teamreflog.reflogserver.team.domain.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class TeamReflectionDaysEmptyException extends TeamException {

    public TeamReflectionDaysEmptyException() {
        super(BAD_REQUEST, "회고일은 최소 하루 이상이어야 합니다.");
    }
}
