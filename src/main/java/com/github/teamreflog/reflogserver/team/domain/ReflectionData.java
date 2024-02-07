package com.github.teamreflog.reflogserver.team.domain;

import java.time.LocalDate;

public record ReflectionData(
        Long reflectionId, Long memberId, Long topicId, String content, LocalDate reflectionAt) {}
