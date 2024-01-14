package com.github.teamreflog.reflogserver.mail.exception;

import com.github.teamreflog.reflogserver.common.exception.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class MailExceptionHandler {

    @ExceptionHandler(MailException.class)
    public ResponseEntity<ErrorResponse> handleMailException(final MailException e) {

        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), e.getStatus());
    }
}
