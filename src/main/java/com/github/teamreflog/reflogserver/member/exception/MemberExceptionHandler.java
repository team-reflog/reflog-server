package com.github.teamreflog.reflogserver.member.exception;

import com.github.teamreflog.reflogserver.common.exception.ErrorResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class MemberExceptionHandler {

    @ExceptionHandler(MemberException.class)
    public ResponseEntity<ErrorResponse> handleMemberException(final MemberException e) {

        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), e.getStatus());
    }
}
