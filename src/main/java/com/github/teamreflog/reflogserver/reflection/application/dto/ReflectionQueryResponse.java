package com.github.teamreflog.reflogserver.reflection.application.dto;

import com.github.teamreflog.reflogserver.reflection.domain.Reflection;

public record ReflectionQueryResponse(Long reflectionId, Long topicId, String content) {

    public static ReflectionQueryResponse fromEntity(final Reflection reflection) {
        return new ReflectionQueryResponse(
                reflection.getId(), reflection.getTopicId(), reflection.getContent());
    }
}
