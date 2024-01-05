package com.github.teamreflog.reflogserver.topic.application.dto;

import com.github.teamreflog.reflogserver.topic.domain.Topic;

public record TopicCreateRequest(Long memberId, Long teamId, String content) {

    public TopicCreateRequest setMemberId(final Long memberId) {
        return new TopicCreateRequest(memberId, teamId, content);
    }

    public Topic toEntity() {
        return Topic.of(teamId, content);
    }
}
