package com.github.teamreflog.reflogserver.topic.application;

import com.github.teamreflog.reflogserver.team.domain.CrewRepository;
import com.github.teamreflog.reflogserver.topic.application.dto.TopicCreateRequest;
import com.github.teamreflog.reflogserver.topic.application.dto.TopicQueryResponse;
import com.github.teamreflog.reflogserver.topic.application.dto.TopicTodayQueryRequest;
import com.github.teamreflog.reflogserver.topic.domain.DayOfWeekProvider;
import com.github.teamreflog.reflogserver.topic.domain.TeamQueryClient;
import com.github.teamreflog.reflogserver.topic.domain.Topic;
import com.github.teamreflog.reflogserver.topic.domain.TopicCreateValidator;
import com.github.teamreflog.reflogserver.topic.domain.TopicRepository;
import com.github.teamreflog.reflogserver.topic.domain.Topics;
import java.time.DayOfWeek;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TopicService {

    private final TeamQueryClient teamQueryClient;
    private final TopicRepository topicRepository;
    private final CrewRepository crewRepository;
    private final DayOfWeekProvider dayOfWeekProvider;
    private final TopicCreateValidator topicCreateValidator;

    public Long createTopic(final Long ownerId, final TopicCreateRequest request) {
        topicCreateValidator.validateTeamOwnerAuthorization(request.teamId(), ownerId);

        final Topic newTopic = request.toEntity();
        final Topics topics = Topics.from(topicRepository.findAllByTeamId(request.teamId()));
        topics.addTopic(newTopic);

        return topicRepository.save(newTopic).getId();
    }

    public List<TopicQueryResponse> queryTopics(final Long teamId) {
        return topicRepository.findAllByTeamId(teamId).stream()
                .map(TopicQueryResponse::fromEntity)
                .toList();
    }

    public List<TopicQueryResponse> queryTodayTopics(final TopicTodayQueryRequest request) {
        final DayOfWeek today = dayOfWeekProvider.getToday(request.timezone());

        return crewRepository.findAllByMemberId(request.memberId()).stream()
                .map(crew -> teamQueryClient.getTeamData(crew.getTeamId()))
                .filter(teamData -> teamData.containsReflectionDay(today))
                .map(teamData -> topicRepository.findAllByTeamId(teamData.id()))
                .flatMap(List::stream)
                .map(TopicQueryResponse::fromEntity)
                .toList();
    }
}
