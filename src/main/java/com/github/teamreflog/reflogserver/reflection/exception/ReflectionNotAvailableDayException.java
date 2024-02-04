package com.github.teamreflog.reflogserver.reflection.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class ReflectionNotAvailableDayException extends ReflectionException {

    public ReflectionNotAvailableDayException() {
        super(BAD_REQUEST, "회고 작성일이 아닙니다.");
    }
}
