package com.github.teamreflog.reflogserver.team.service;

import com.github.teamreflog.reflogserver.auth.dto.AuthPrincipal;
import com.github.teamreflog.reflogserver.team.dto.TeamCreateRequest;
import com.github.teamreflog.reflogserver.team.dto.TeamQueryResponse;
import com.github.teamreflog.reflogserver.team.exception.TeamNameDuplicatedException;
import com.github.teamreflog.reflogserver.team.exception.TeamNotExistException;
import com.github.teamreflog.reflogserver.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    public void createTeam(final AuthPrincipal authPrincipal, final TeamCreateRequest request) {
        if (teamRepository.existsByName(request.name())) {
            throw new TeamNameDuplicatedException();
        }

        teamRepository.save(request.toEntity(authPrincipal.memberId()));
    }

    public TeamQueryResponse queryTeam(final Long teamId) {
        return teamRepository
                .findById(teamId)
                .map(TeamQueryResponse::fromEntity)
                .orElseThrow(TeamNotExistException::new);
    }
}
