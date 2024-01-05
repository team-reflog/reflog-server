package com.github.teamreflog.reflogserver.topic.domain;

import com.github.teamreflog.reflogserver.topic.domain.exception.NotOwnerException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TopicCreateValidator {

    private final TeamQueryClient teamQueryClient;

    public void validateTeamOwnerAuthorization(final Long teamId, final Long memberId) {
        final TeamData data = teamQueryClient.getTeamData(teamId);

        validateTeamOwnerAuthorization(data, memberId);
    }

    void validateTeamOwnerAuthorization(final TeamData data, final Long memberId) {
        if (!data.isOwner(memberId)) {
            throw new NotOwnerException();
        }
    }
}
