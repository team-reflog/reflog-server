package com.github.teamreflog.reflogserver.reflection.infrastructure;

import com.github.teamreflog.reflogserver.common.exception.ReflogIllegalArgumentException;
import com.github.teamreflog.reflogserver.reflection.domain.TopicData;
import com.github.teamreflog.reflogserver.reflection.domain.TopicQueryService;
import com.github.teamreflog.reflogserver.topic.domain.Topic;
import com.github.teamreflog.reflogserver.topic.domain.TopicRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TopicQueryClient implements TopicQueryService {

    private final TopicRepository topicRepository;

    @Override
    public TopicData getTopicDataById(final Long topicId) {
        final Topic topic =
                topicRepository.findById(topicId).orElseThrow(ReflogIllegalArgumentException::new);

        return new TopicData(topic.getId(), topic.getContent());
    }

    @Override
    public List<TopicData> getAllTopicDataByTeamId(final Long teamId) {
        return topicRepository.findAllByTeamId(teamId).stream()
                .map(topic -> new TopicData(topic.getId(), topic.getContent()))
                .toList();
    }
}
