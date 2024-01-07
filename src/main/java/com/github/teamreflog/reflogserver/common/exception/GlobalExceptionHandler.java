package com.github.teamreflog.reflogserver.common.exception;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;

import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Order(Ordered.LOWEST_PRECEDENCE)
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ErrorResponse> handleRuntimeException(final RuntimeException e) {
        log.error("Unexpected error processing request", e);

        return new ResponseEntity<>(
                new ErrorResponse("일시적인 에러가 발생했습니다. 잠시 후 다시 시도해주세요."), INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ReflogIllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handleReflogIllegalArgumentException(
            final ReflogIllegalArgumentException e) {
        log.info("Requested with rong argument", e);

        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), e.getStatus());
    }
}
