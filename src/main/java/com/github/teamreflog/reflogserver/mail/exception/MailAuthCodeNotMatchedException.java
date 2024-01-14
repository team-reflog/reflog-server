package com.github.teamreflog.reflogserver.mail.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class MailAuthCodeNotMatchedException extends MailException {

    public MailAuthCodeNotMatchedException() {
        super(BAD_REQUEST, "인증 코드가 일치하지 않습니다.");
    }
}
