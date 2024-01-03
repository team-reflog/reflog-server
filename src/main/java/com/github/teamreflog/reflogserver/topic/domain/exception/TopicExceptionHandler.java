package com.github.teamreflog.reflogserver.topic.domain.exception;

import com.github.teamreflog.reflogserver.common.exception.ErrorResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Order(Ordered.HIGHEST_PRECEDENCE)
@RestControllerAdvice
public class TopicExceptionHandler {

    @ExceptionHandler(TopicException.class)
    public ResponseEntity<ErrorResponse> handleTopicException(final TopicException e) {
        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), e.getStatus());
    }
}
