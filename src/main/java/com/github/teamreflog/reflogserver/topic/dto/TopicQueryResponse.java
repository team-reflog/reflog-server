package com.github.teamreflog.reflogserver.topic.dto;

import com.github.teamreflog.reflogserver.topic.domain.Topic;

public record TopicQueryResponse(Long id, Long teamId, String content) {

    public static TopicQueryResponse fromEntity(final Topic topic) {
        return new TopicQueryResponse(topic.getId(), topic.getTeamId(), topic.getContent());
    }
}
