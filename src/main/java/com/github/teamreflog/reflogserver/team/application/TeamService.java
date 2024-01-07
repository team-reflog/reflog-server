package com.github.teamreflog.reflogserver.team.application;

import com.github.teamreflog.reflogserver.common.exception.ReflogIllegalArgumentException;
import com.github.teamreflog.reflogserver.team.application.dto.CrewQueryResponse;
import com.github.teamreflog.reflogserver.team.application.dto.TeamCreateRequest;
import com.github.teamreflog.reflogserver.team.application.dto.TeamQueryResponse;
import com.github.teamreflog.reflogserver.team.domain.Crew;
import com.github.teamreflog.reflogserver.team.domain.CrewRepository;
import com.github.teamreflog.reflogserver.team.domain.Team;
import com.github.teamreflog.reflogserver.team.domain.TeamRepository;
import com.github.teamreflog.reflogserver.team.domain.TeamValidator;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final CrewRepository crewRepository;
    private final TeamValidator teamValidator;

    @Transactional
    public Long createTeam(final TeamCreateRequest request) {
        teamValidator.validateDuplicateTeamName(request.name());
        final Team team = teamRepository.save(request.toEntity());

        crewRepository.save(Crew.of(team.getId(), request.ownerId(), request.nickname()));

        return team.getId();
    }

    public TeamQueryResponse queryTeam(final Long teamId) {
        return teamRepository
                .findById(teamId)
                .map(TeamQueryResponse::fromEntity)
                .orElseThrow(ReflogIllegalArgumentException::new);
    }

    public List<CrewQueryResponse> queryCrews(final Long teamId) {
        final Team team =
                teamRepository.findById(teamId).orElseThrow(ReflogIllegalArgumentException::new);

        return crewRepository.findAllByTeamId(teamId).stream()
                .map(crew -> CrewQueryResponse.fromEntity(crew, team))
                .toList();
    }
}
