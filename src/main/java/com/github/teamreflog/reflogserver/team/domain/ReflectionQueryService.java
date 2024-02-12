package com.github.teamreflog.reflogserver.team.domain;

import java.time.LocalDate;
import java.util.List;

public interface ReflectionQueryService {

    List<ReflectionData> getAllTeamReflectionsInTopicsByDate(
            List<Long> topicIds, LocalDate reflectionDate);
}
