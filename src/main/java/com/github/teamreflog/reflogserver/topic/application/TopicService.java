package com.github.teamreflog.reflogserver.topic.application;

import com.github.teamreflog.reflogserver.team.domain.CrewRepository;
import com.github.teamreflog.reflogserver.team.domain.Team;
import com.github.teamreflog.reflogserver.team.domain.TeamRepository;
import com.github.teamreflog.reflogserver.topic.application.dto.TopicCreateRequest;
import com.github.teamreflog.reflogserver.topic.application.dto.TopicQueryResponse;
import com.github.teamreflog.reflogserver.topic.application.dto.TopicTodayQueryRequest;
import com.github.teamreflog.reflogserver.topic.domain.DayOfWeekProvider;
import com.github.teamreflog.reflogserver.topic.domain.Topic;
import com.github.teamreflog.reflogserver.topic.domain.TopicRepository;
import com.github.teamreflog.reflogserver.topic.domain.Topics;
import com.github.teamreflog.reflogserver.topic.domain.exception.NotOwnerException;
import com.github.teamreflog.reflogserver.topic.domain.exception.TeamNotExistException;
import java.time.DayOfWeek;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TeamRepository teamRepository;
    private final TopicRepository topicRepository;
    private final CrewRepository crewRepository;
    private final DayOfWeekProvider dayOfWeekProvider;

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

    // TODO: refactoring
    @Transactional(readOnly = true)
    public List<TopicQueryResponse> queryTodayTopics(final TopicTodayQueryRequest request) {
        final DayOfWeek today = dayOfWeekProvider.getToday(request.timezone());

        return crewRepository.findAllByMemberId(request.memberId()).stream()
                .map(
                        crew ->
                                teamRepository
                                        .findById(crew.getTeamId())
                                        .orElseThrow(IllegalStateException::new))
                .filter(team -> team.containsReflectionDay(today))
                .map(team -> topicRepository.findAllByTeamId(team.getId()))
                .flatMap(List::stream)
                .map(TopicQueryResponse::fromEntity)
                .toList();
    }
}
