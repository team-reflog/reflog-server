package com.github.teamreflog.reflogserver.common.exception;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.LOWEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(final RuntimeException e) {
        // TODO: logging cause
        return new ResponseEntity<>(
                new ErrorResponse("일시적인 에러가 발생했습니다. 잠시 후 다시 시도해주세요."), INTERNAL_SERVER_ERROR);
    }
}
