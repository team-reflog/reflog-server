package com.github.teamreflog.reflogserver.topic.domain.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class TeamNotExistException extends TopicException {

    public TeamNotExistException() {
        super(BAD_REQUEST, "존재하지 않는 팀입니다.");
    }
}
