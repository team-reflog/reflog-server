package com.github.teamreflog.reflogserver.team.domain;

import com.github.teamreflog.reflogserver.team.domain.exception.TeamNameDuplicatedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeamValidator {

    private final TeamRepository teamRepository;

    public void validateDuplicateTeamName(final String name) {
        if (teamRepository.existsByName(name)) {
            throw new TeamNameDuplicatedException();
        }
    }
}
