package com.github.teamreflog.reflogserver.topic.infrastructure;

import com.github.teamreflog.reflogserver.team.application.TeamService;
import com.github.teamreflog.reflogserver.team.application.dto.TeamQueryResponse;
import com.github.teamreflog.reflogserver.topic.domain.TeamData;
import com.github.teamreflog.reflogserver.topic.domain.TeamQueryClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeamClient implements TeamQueryClient {

    private final TeamService teamService;

    @Override
    public TeamData getTeamData(final Long teamId) {
        final TeamQueryResponse response = teamService.queryTeam(teamId);

        return new TeamData(response.id(), response.ownerId(), response.daysOfWeek());
    }
}
