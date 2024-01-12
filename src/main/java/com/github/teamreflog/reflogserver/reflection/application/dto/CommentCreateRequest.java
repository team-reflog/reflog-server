package com.github.teamreflog.reflogserver.reflection.application.dto;

public record CommentCreateRequest(Long memberId, Long reflectionId, String content) {

    public CommentCreateRequest setMemberId(final Long memberId) {
        return new CommentCreateRequest(memberId, reflectionId, content);
    }

    public CommentCreateRequest setReflectionId(final Long reflectionId) {
        return new CommentCreateRequest(memberId, reflectionId, content);
    }
}
