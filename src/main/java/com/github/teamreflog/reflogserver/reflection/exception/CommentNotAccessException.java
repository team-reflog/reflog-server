package com.github.teamreflog.reflogserver.reflection.exception;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

public class CommentNotAccessException extends CommentException {

    public CommentNotAccessException(final Throwable cause) {
        super(BAD_REQUEST, "댓글을 작성할 수 없습니다.", cause);
    }
}
