package com.github.teamreflog.reflogserver.team.infrastructure;

import com.github.teamreflog.reflogserver.team.domain.TopicData;
import com.github.teamreflog.reflogserver.team.domain.TopicQueryService;
import com.github.teamreflog.reflogserver.topic.domain.TopicRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TopicClient implements TopicQueryService {

    private final TopicRepository topicRepository;

    @Override
    public List<TopicData> getAllTopicDataByTeamId(final Long teamId) {
        return topicRepository.findAllByTeamId(teamId).stream()
                .map(topic -> new TopicData(topic.getId(), topic.getContent()))
                .toList();
    }
}
