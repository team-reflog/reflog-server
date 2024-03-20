package com.github.teamreflog.reflogserver.reflection.domain;

import org.springframework.stereotype.Component;

@Component
public interface TeamQueryService {

    TeamData getTeamDataByTopicId(long topicId);
}
