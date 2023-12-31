package com.github.teamreflog.reflogserver.team.service;

import com.github.teamreflog.reflogserver.team.dto.TeamCreateRequest;
import com.github.teamreflog.reflogserver.team.exception.TeamNameDuplicatedException;
import com.github.teamreflog.reflogserver.team.repository.TeamRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TeamService {

    private final TeamRepository teamRepository;

    public void createTeam(final TeamCreateRequest request) {
        if (teamRepository.existsByName(request.name())) {
            throw new TeamNameDuplicatedException();
        }

        // TODO: 토큰에서 사용자 ID를 추출하여 사용하도록 수정
        teamRepository.save(request.toEntity(777L));
    }
}
