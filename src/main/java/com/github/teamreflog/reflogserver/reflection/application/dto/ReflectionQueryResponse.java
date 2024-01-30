package com.github.teamreflog.reflogserver.reflection.application.dto;

import com.github.teamreflog.reflogserver.reflection.domain.Reflection;
import com.github.teamreflog.reflogserver.reflection.domain.TopicData;

public record ReflectionQueryResponse(
        Long reflectionId, Long topicId, String topicContent, String content) {

    public static ReflectionQueryResponse fromEntity(
            final Reflection reflection, final TopicData topicData) {
        return new ReflectionQueryResponse(
                reflection.getId(),
                reflection.getTopicId(),
                topicData.content(),
                reflection.getContent());
    }
}
