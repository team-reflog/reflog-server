package com.github.teamreflog.reflogserver.reflection.application.dto;

import com.github.teamreflog.reflogserver.reflection.domain.Reflection;
import java.time.LocalDate;

public record ReflectionCreateRequest(
        Long memberId, Long topicId, String content, String timezone) {

    public ReflectionCreateRequest setMemberId(final Long memberId) {
        return new ReflectionCreateRequest(memberId, topicId, content, timezone);
    }

    public ReflectionCreateRequest setTimezone(final String timezone) {
        return new ReflectionCreateRequest(memberId, topicId, content, timezone);
    }

    public Reflection toEntity(final LocalDate localDate) {
        return Reflection.of(memberId, topicId, content, localDate);
    }
}
