package com.github.teamreflog.reflogserver.topic.domain.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class NotOwnerException extends TopicException {

    public NotOwnerException() {
        super(BAD_REQUEST, "팀장만 주제를 생성할 수 있습니다.");
    }
}
