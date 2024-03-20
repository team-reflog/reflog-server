package com.github.teamreflog.reflogserver.reflection.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class ReflectionAlreadyExistException extends ReflectionException {

    public ReflectionAlreadyExistException() {
        super(BAD_REQUEST, "이미 오늘의 회고를 작성했습니다.");
    }
}
