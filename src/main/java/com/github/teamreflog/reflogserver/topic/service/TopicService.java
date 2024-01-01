package com.github.teamreflog.reflogserver.topic.service;

import com.github.teamreflog.reflogserver.team.domain.Team;
import com.github.teamreflog.reflogserver.team.repository.TeamRepository;
import com.github.teamreflog.reflogserver.topic.dto.TopicCreateRequest;
import com.github.teamreflog.reflogserver.topic.exception.NotOwnerException;
import com.github.teamreflog.reflogserver.topic.exception.TeamNotExistException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TeamRepository teamRepository;

    public Long createTopic(final Long ownerId, final TopicCreateRequest request) {
        final Team team =
                teamRepository.findById(request.teamId()).orElseThrow(TeamNotExistException::new);

        if (!team.isOwner(ownerId)) {
            throw new NotOwnerException();
        }

        return 1L;
    }
}
