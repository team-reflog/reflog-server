package com.github.teamreflog.reflogserver.team.service;

import com.github.teamreflog.reflogserver.auth.dto.AuthPrincipal;
import com.github.teamreflog.reflogserver.team.dto.TeamCreateRequest;
import com.github.teamreflog.reflogserver.team.dto.TeamQueryResponse;
import com.github.teamreflog.reflogserver.team.exception.TeamNameDuplicatedException;
import com.github.teamreflog.reflogserver.team.exception.TeamNotExistException;
import com.github.teamreflog.reflogserver.team.exception.TeamReflectionDaysEmptyException;
import com.github.teamreflog.reflogserver.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    public Long createTeam(final AuthPrincipal authPrincipal, final TeamCreateRequest request) {
        if (request.reflectionDays().isEmpty()) {
            throw new TeamReflectionDaysEmptyException();
        }
        if (teamRepository.existsByName(request.name())) {
            throw new TeamNameDuplicatedException();
        }

        return teamRepository.save(request.toEntity(authPrincipal.memberId())).getId();
    }

    public TeamQueryResponse queryTeam(final Long teamId) {
        return teamRepository
                .findById(teamId)
                .map(TeamQueryResponse::fromEntity)
                .orElseThrow(TeamNotExistException::new);
    }
}
