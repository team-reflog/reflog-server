package com.github.teamreflog.reflogserver.common.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class ReflogIllegalArgumentException extends ReflogException {

    public ReflogIllegalArgumentException() {
        super(BAD_REQUEST, "잘못된 요청 값입니다.");
    }
}
