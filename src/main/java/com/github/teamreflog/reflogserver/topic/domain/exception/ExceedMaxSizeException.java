package com.github.teamreflog.reflogserver.topic.domain.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class ExceedMaxSizeException extends TopicException {

    public ExceedMaxSizeException() {
        super(BAD_REQUEST, "주제의 최대 개수를 초과하였습니다.");
    }
}
