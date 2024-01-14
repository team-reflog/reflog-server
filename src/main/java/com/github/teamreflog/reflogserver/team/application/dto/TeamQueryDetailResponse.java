package com.github.teamreflog.reflogserver.team.application.dto;

import com.github.teamreflog.reflogserver.team.domain.Team;
import com.github.teamreflog.reflogserver.team.domain.TopicData;
import java.time.DayOfWeek;
import java.util.List;

public record TeamQueryDetailResponse(
        String name,
        String description,
        Long ownerId,
        List<DayOfWeek> reflectionDays,
        List<TopicData> topics) {

    public static TeamQueryDetailResponse from(final Team team, final List<TopicData> topicData) {

        return new TeamQueryDetailResponse(
                team.getName(),
                team.getDescription(),
                team.getOwnerId(),
                team.getReflectionDays(),
                topicData);
    }
}
