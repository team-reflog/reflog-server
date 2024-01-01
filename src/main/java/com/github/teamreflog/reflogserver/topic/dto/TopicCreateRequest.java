package com.github.teamreflog.reflogserver.topic.dto;

import com.github.teamreflog.reflogserver.topic.domain.Topic;

public record TopicCreateRequest(Long teamId, String content) {

    public Topic toEntity() {
        return Topic.of(teamId, content);
    }
}