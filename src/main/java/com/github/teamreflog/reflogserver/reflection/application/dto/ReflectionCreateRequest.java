package com.github.teamreflog.reflogserver.reflection.application.dto;

public record ReflectionCreateRequest(
        Long memberId, Long topicId, String content, String timezone) {

    public ReflectionCreateRequest setMemberId(final Long memberId) {
        return new ReflectionCreateRequest(memberId, topicId, content, timezone);
    }

    public ReflectionCreateRequest setTimezone(final String timezone) {
        return new ReflectionCreateRequest(memberId, topicId, content, timezone);
    }
}
