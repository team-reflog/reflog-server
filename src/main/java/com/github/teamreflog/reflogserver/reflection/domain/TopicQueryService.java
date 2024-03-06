package com.github.teamreflog.reflogserver.reflection.domain;

import java.util.List;

public interface TopicQueryService {

    TopicData getTopicDataById(Long topicId);

    List<TopicData> getAllTopicDataByTeamId(final Long teamId);
}
