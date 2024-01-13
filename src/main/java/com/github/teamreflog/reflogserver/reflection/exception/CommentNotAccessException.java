package com.github.teamreflog.reflogserver.reflection.exception;

import static org.springframework.http.HttpStatus.UNAUTHORIZED;

public class CommentNotAccessException extends CommentException {

    public CommentNotAccessException(final Throwable cause) {
        super(UNAUTHORIZED, "댓글을 작성할 권한이 없습니다.", cause);
    }
}
