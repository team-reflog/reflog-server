package com.github.teamreflog.reflogserver.team.domain;

import com.github.teamreflog.reflogserver.common.exception.ReflogIllegalArgumentException;
import com.github.teamreflog.reflogserver.team.domain.exception.InviteNotAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class InviteValidator {

    private final TeamRepository teamRepository;

    public void validateTeamOwnerAuthorization(final Long memberId, final Long teamId) {
        final Team team =
                teamRepository.findById(teamId).orElseThrow(ReflogIllegalArgumentException::new);

        validateTeamOwnerAuthorization(memberId, team);
    }

    void validateTeamOwnerAuthorization(final Long memberId, final Team team) {
        if (!team.isOwner(memberId)) {
            throw new InviteNotAccessException();
        }
    }
}
