package com.github.teamreflog.reflogserver.reflection.infrastructure;

import com.github.teamreflog.reflogserver.common.exception.ReflogIllegalArgumentException;
import com.github.teamreflog.reflogserver.reflection.domain.TeamData;
import com.github.teamreflog.reflogserver.reflection.domain.TeamQueryService;
import com.github.teamreflog.reflogserver.team.domain.Team;
import com.github.teamreflog.reflogserver.team.domain.TeamRepository;
import com.github.teamreflog.reflogserver.topic.domain.Topic;
import com.github.teamreflog.reflogserver.topic.domain.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class TeamQueryServiceImpl implements TeamQueryService {

    private final TeamRepository teamRepository;
    private final TopicRepository topicRepository;

    @Override
    public TeamData getTeamDataByTopicId(final long topicId) {
        final Topic topic =
                topicRepository.findById(topicId).orElseThrow(ReflogIllegalArgumentException::new);

        final Team team =
                teamRepository
                        .findById(topic.getTeamId())
                        .orElseThrow(ReflogIllegalArgumentException::new);

        return new TeamData(team.getReflectionDays());
    }
}
