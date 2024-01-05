package com.github.teamreflog.reflogserver.reflection.application.dto;

import com.github.teamreflog.reflogserver.reflection.domain.Reflection;

public record ReflectionCreateRequest(Long memberId, Long topicId, String content) {

    public ReflectionCreateRequest setMemberId(final Long memberId) {
        return new ReflectionCreateRequest(memberId, topicId, content);
    }

    public Reflection toEntity() {
        return Reflection.of(memberId, topicId, content);
    }
}
