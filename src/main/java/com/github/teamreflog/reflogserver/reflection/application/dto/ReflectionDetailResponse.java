package com.github.teamreflog.reflogserver.reflection.application.dto;

import com.github.teamreflog.reflogserver.reflection.domain.CrewData;
import com.github.teamreflog.reflogserver.reflection.domain.Reflection;
import java.time.LocalDate;

public record ReflectionDetailResponse(
        Long memberId,
        String nickname,
        Long reflectionId,
        Long topicId,
        String content,
        LocalDate reflectionAt) {

    public static ReflectionDetailResponse fromEntity(
            final CrewData crewData, final Reflection reflection) {
        return new ReflectionDetailResponse(
                crewData.crewId(),
                crewData.nickname(),
                reflection.getId(),
                reflection.getTopicId(),
                reflection.getContent(),
                reflection.getReflectionDate());
    }
}
