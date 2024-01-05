package com.github.teamreflog.reflogserver.topic.application.dto;

public record TopicTodayQueryRequest(Long memberId, String timezone) {

    public TopicTodayQueryRequest setMemberId(final Long memberId) {
        return new TopicTodayQueryRequest(memberId, timezone);
    }
}
