package com.github.teamreflog.reflogserver.team.domain;

import com.github.teamreflog.reflogserver.team.domain.exception.TeamNotExistException;
import com.github.teamreflog.reflogserver.topic.domain.exception.NotOwnerException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InviteValidator {

    private final TeamRepository teamRepository;

    public void validateTeamOwnerAuthorization(final Long memberId, final Long teamId) {
        final Team team = teamRepository.findById(teamId).orElseThrow(TeamNotExistException::new);

        validateTeamOwnerAuthorization(memberId, team);
    }

    void validateTeamOwnerAuthorization(final Long memberId, final Team team) {
        if (!team.isOwner(memberId)) {
            throw new NotOwnerException();
        }
    }
}
