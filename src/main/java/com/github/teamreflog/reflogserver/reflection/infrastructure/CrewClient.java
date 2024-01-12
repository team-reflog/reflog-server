package com.github.teamreflog.reflogserver.reflection.infrastructure;

import com.github.teamreflog.reflogserver.common.exception.ReflogIllegalArgumentException;
import com.github.teamreflog.reflogserver.reflection.domain.CrewData;
import com.github.teamreflog.reflogserver.reflection.domain.CrewQueryClient;
import com.github.teamreflog.reflogserver.reflection.domain.Reflection;
import com.github.teamreflog.reflogserver.reflection.domain.ReflectionRepository;
import com.github.teamreflog.reflogserver.team.domain.CrewRepository;
import com.github.teamreflog.reflogserver.topic.domain.Topic;
import com.github.teamreflog.reflogserver.topic.domain.TopicRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CrewClient implements CrewQueryClient {

    private final CrewRepository crewRepository;
    private final TopicRepository topicRepository;
    private final ReflectionRepository reflectionRepository;

    @Override
    public CrewData getCrewDataById(final Long id) {
        return crewRepository
                .findById(id)
                .map(crew -> new CrewData(crew.getId(), crew.getNickname()))
                .orElseThrow(ReflogIllegalArgumentException::new);
    }

    @Override
    public CrewData getCrewDataByMemberIdAndReflectionId(
            final Long memberId, final Long reflectionId) {
        final Reflection reflection =
                reflectionRepository
                        .findById(reflectionId)
                        .orElseThrow(ReflogIllegalArgumentException::new);

        final Topic topic =
                topicRepository
                        .findById(reflection.getTopicId())
                        .orElseThrow(ReflogIllegalArgumentException::new);

        return crewRepository
                .findByMemberIdAndTeamId(memberId, topic.getTeamId())
                .map(crew -> new CrewData(crew.getId(), crew.getNickname()))
                .orElseThrow(ReflogIllegalArgumentException::new);
    }
}
