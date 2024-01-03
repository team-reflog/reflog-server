package com.github.teamreflog.reflogserver.team.domain.exception;

import org.springframework.http.HttpStatus;

public class CrewAlreadyJoinedException extends TeamException {

    public CrewAlreadyJoinedException() {
        super(HttpStatus.BAD_REQUEST, "이미 팀원입니다.");
    }
}
