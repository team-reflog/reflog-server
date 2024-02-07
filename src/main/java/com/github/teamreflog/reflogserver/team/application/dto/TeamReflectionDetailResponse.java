package com.github.teamreflog.reflogserver.team.application.dto;

import com.github.teamreflog.reflogserver.team.domain.Crew;
import com.github.teamreflog.reflogserver.team.domain.ReflectionData;
import java.time.LocalDate;

public record TeamReflectionDetailResponse(
        Long memberId,
        String nickname,
        Long reflectionId,
        Long topicId,
        String content,
        LocalDate reflectionAt) {

    public static TeamReflectionDetailResponse fromEntity(
            final Crew crew, final ReflectionData reflectionData) {
        return new TeamReflectionDetailResponse(
                crew.getMemberId(),
                crew.getNickname(),
                reflectionData.reflectionId(),
                reflectionData.topicId(),
                reflectionData.content(),
                reflectionData.reflectionAt());
    }
}
