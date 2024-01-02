package com.github.teamreflog.reflogserver.team.service;

import com.github.teamreflog.reflogserver.auth.dto.AuthPrincipal;
import com.github.teamreflog.reflogserver.team.domain.Team;
import com.github.teamreflog.reflogserver.team.domain.TeamMember;
import com.github.teamreflog.reflogserver.team.domain.TeamMemberRepository;
import com.github.teamreflog.reflogserver.team.dto.TeamCreateRequest;
import com.github.teamreflog.reflogserver.team.dto.TeamMemberQueryResponse;
import com.github.teamreflog.reflogserver.team.dto.TeamQueryResponse;
import com.github.teamreflog.reflogserver.team.exception.TeamNameDuplicatedException;
import com.github.teamreflog.reflogserver.team.exception.TeamNotExistException;
import com.github.teamreflog.reflogserver.team.exception.TeamReflectionDaysEmptyException;
import com.github.teamreflog.reflogserver.team.repository.TeamRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;

    @Transactional
    public Long createTeam(final AuthPrincipal authPrincipal, final TeamCreateRequest request) {
        if (request.reflectionDays().isEmpty()) {
            throw new TeamReflectionDaysEmptyException();
        }
        if (teamRepository.existsByName(request.name())) {
            throw new TeamNameDuplicatedException();
        }

        final Team team = teamRepository.save(request.toEntity(authPrincipal.memberId()));

        teamMemberRepository.save(
                TeamMember.of(team.getId(), authPrincipal.memberId(), request.nickname()));

        return team.getId();
    }

    public TeamQueryResponse queryTeam(final Long teamId) {
        return teamRepository
                .findById(teamId)
                .map(TeamQueryResponse::fromEntity)
                .orElseThrow(TeamNotExistException::new);
    }

    public List<TeamMemberQueryResponse> queryTeamMembers(final Long teamId) {
        final Team team = teamRepository.findById(teamId).orElseThrow(TeamNotExistException::new);

        return teamMemberRepository.findAllByTeamId(teamId).stream()
                .map(teamMember -> TeamMemberQueryResponse.fromEntity(teamMember, team))
                .toList();
    }
}
