package com.github.teamreflog.reflogserver.team.application;

import com.github.teamreflog.reflogserver.common.exception.ReflogIllegalArgumentException;
import com.github.teamreflog.reflogserver.team.application.dto.CrewQueryResponse;
import com.github.teamreflog.reflogserver.team.application.dto.TeamCreateRequest;
import com.github.teamreflog.reflogserver.team.application.dto.TeamQueryDetailRequest;
import com.github.teamreflog.reflogserver.team.application.dto.TeamQueryDetailResponse;
import com.github.teamreflog.reflogserver.team.application.dto.TeamQueryResponse;
import com.github.teamreflog.reflogserver.team.domain.Crew;
import com.github.teamreflog.reflogserver.team.domain.CrewRepository;
import com.github.teamreflog.reflogserver.team.domain.Team;
import com.github.teamreflog.reflogserver.team.domain.TeamRepository;
import com.github.teamreflog.reflogserver.team.domain.TeamValidator;
import com.github.teamreflog.reflogserver.team.domain.TopicData;
import com.github.teamreflog.reflogserver.team.domain.TopicQueryService;
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
    private final TopicQueryService topicQueryService;

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

    @Transactional(readOnly = true)
    public List<TeamQueryResponse> queryTeams(final Long memberId) {
        final List<Long> teamIds =
                crewRepository.findAllByMemberId(memberId).stream().map(Crew::getTeamId).toList();

        return teamRepository.findAllByIdIn(teamIds).stream()
                .map(TeamQueryResponse::fromEntity)
                .toList();
    }

    public List<CrewQueryResponse> queryCrews(final Long teamId) {
        final Team team =
                teamRepository.findById(teamId).orElseThrow(ReflogIllegalArgumentException::new);

        return crewRepository.findAllByTeamId(teamId).stream()
                .map(crew -> CrewQueryResponse.fromEntity(crew, team))
                .toList();
    }

    @Transactional(readOnly = true)
    public TeamQueryDetailResponse queryTeamDetails(final TeamQueryDetailRequest request) {
        final List<Crew> crews = crewRepository.findAllByTeamId(request.teamId());
        if (crews.stream().noneMatch(crew -> crew.isSameMemberId(request.memberId()))) {
            throw new ReflogIllegalArgumentException();
        }

        final Team team =
                teamRepository
                        .findById(request.teamId())
                        .orElseThrow(ReflogIllegalArgumentException::new);
        final List<TopicData> topicData =
                topicQueryService.getAllTopicDataByTeamId(request.teamId());

        return TeamQueryDetailResponse.from(team, crews, topicData);
    }
}
