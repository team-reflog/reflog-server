package com.github.teamreflog.reflogserver.reflection.application.dto;

public record ReflectionCreateRequest(Long writerId, Long topicId, String content) {}
