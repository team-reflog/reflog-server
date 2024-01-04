package com.github.teamreflog.reflogserver.topic.application;

import com.github.teamreflog.reflogserver.team.domain.Team;
import com.github.teamreflog.reflogserver.team.domain.TeamRepository;
import com.github.teamreflog.reflogserver.topic.application.dto.TopicCreateRequest;
import com.github.teamreflog.reflogserver.topic.application.dto.TopicQueryResponse;
import com.github.teamreflog.reflogserver.topic.domain.Topic;
import com.github.teamreflog.reflogserver.topic.domain.TopicRepository;
import com.github.teamreflog.reflogserver.topic.domain.Topics;
import com.github.teamreflog.reflogserver.topic.domain.exception.NotOwnerException;
import com.github.teamreflog.reflogserver.topic.domain.exception.TeamNotExistException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TeamRepository teamRepository;
    private final TopicRepository topicRepository;

    public Long createTopic(final Long ownerId, final TopicCreateRequest request) {
        final Team team =
                teamRepository.findById(request.teamId()).orElseThrow(TeamNotExistException::new);
        if (!team.isOwner(ownerId)) {
            throw new NotOwnerException();
        }

        final Topic newTopic = request.toEntity();
        final Topics topics = Topics.from(topicRepository.findAllByTeamId(team.getId()));
        topics.addTopic(newTopic);

        return topicRepository.save(newTopic).getId();
    }

    public List<TopicQueryResponse> queryTopics(final Long teamId) {
        return topicRepository.findAllByTeamId(teamId).stream()
                .map(TopicQueryResponse::fromEntity)
                .toList();
    }
}
