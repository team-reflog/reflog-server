package com.github.teamreflog.reflogserver.team.application;

import com.github.teamreflog.reflogserver.auth.application.dto.AuthPrincipal;
import com.github.teamreflog.reflogserver.team.application.dto.CrewQueryResponse;
import com.github.teamreflog.reflogserver.team.application.dto.TeamCreateRequest;
import com.github.teamreflog.reflogserver.team.application.dto.TeamQueryResponse;
import com.github.teamreflog.reflogserver.team.domain.Crew;
import com.github.teamreflog.reflogserver.team.domain.Team;
import com.github.teamreflog.reflogserver.team.domain.TeamRepository;
import com.github.teamreflog.reflogserver.team.domain.exception.TeamNameDuplicatedException;
import com.github.teamreflog.reflogserver.team.domain.exception.TeamNotExistException;
import com.github.teamreflog.reflogserver.team.domain.exception.TeamReflectionDaysEmptyException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    @Transactional
    public Long createTeam(final AuthPrincipal authPrincipal, final TeamCreateRequest request) {
        if (request.reflectionDays().isEmpty()) {
            throw new TeamReflectionDaysEmptyException();
        }
        if (teamRepository.existsByName(request.name())) {
            throw new TeamNameDuplicatedException();
        }

        final Team team = request.toEntity(authPrincipal.memberId());
        team.addCrew(Crew.of(authPrincipal.memberId(), request.nickname()));

        return teamRepository.save(team).getId();
    }

    public TeamQueryResponse queryTeam(final Long teamId) {
        return teamRepository
                .findById(teamId)
                .map(TeamQueryResponse::fromEntity)
                .orElseThrow(TeamNotExistException::new);
    }

    public List<CrewQueryResponse> queryCrews(final Long teamId) {
        final Team team = teamRepository.findById(teamId).orElseThrow(TeamNotExistException::new);

        return team.getCrews().stream()
                .map(crew -> CrewQueryResponse.fromEntity(crew, team))
                .toList();
    }
}
