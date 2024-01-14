package com.github.teamreflog.reflogserver.team.domain;

import java.util.List;

public interface TopicQueryService {

    List<TopicData> getAllTopicDataByTeamId(Long teamId);
}
