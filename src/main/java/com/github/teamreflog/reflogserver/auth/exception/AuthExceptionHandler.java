package com.github.teamreflog.reflogserver.auth.exception;

import com.github.teamreflog.reflogserver.common.exception.ErrorResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class AuthExceptionHandler {

    @ExceptionHandler(AuthException.class)
    public ResponseEntity<ErrorResponse> handleAuthException(final AuthException e) {
        log.debug("AuthException: {}", e.getMessage(), e);

        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), e.getStatus());
    }
}
