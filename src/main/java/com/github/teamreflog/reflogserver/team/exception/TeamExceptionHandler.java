package com.github.teamreflog.reflogserver.team.exception;

import com.github.teamreflog.reflogserver.common.exception.ErrorResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class TeamExceptionHandler {

    @ExceptionHandler(TeamException.class)
    public ResponseEntity<ErrorResponse> handleTeamException(final TeamException e) {

        return new ResponseEntity<>(new ErrorResponse(e.getMessage()), e.getStatus());
    }
}
