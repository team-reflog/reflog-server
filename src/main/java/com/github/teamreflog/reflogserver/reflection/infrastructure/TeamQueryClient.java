package com.github.teamreflog.reflogserver.reflection.infrastructure;

import com.github.teamreflog.reflogserver.common.exception.ReflogIllegalArgumentException;
import com.github.teamreflog.reflogserver.reflection.domain.TeamData;
import com.github.teamreflog.reflogserver.reflection.domain.TeamQueryService;
import com.github.teamreflog.reflogserver.team.domain.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeamQueryClient implements TeamQueryService {

    private final TeamRepository teamRepository;

    @Override
    public TeamData getTeamDataById(Long id) {
        return teamRepository
                .findById(id)
                .map(team -> new TeamData(team.getId(), team.getName()))
                .orElseThrow(ReflogIllegalArgumentException::new);
    }
}
