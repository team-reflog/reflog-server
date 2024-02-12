package com.github.teamreflog.reflogserver.team.infrastructure;

import com.github.teamreflog.reflogserver.reflection.domain.ReflectionRepository;
import com.github.teamreflog.reflogserver.team.domain.ReflectionData;
import com.github.teamreflog.reflogserver.team.domain.ReflectionQueryService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReflectionClient implements ReflectionQueryService {

    private final ReflectionRepository reflectionRepository;

    @Override
    public List<ReflectionData> getAllTeamReflectionsInTopicsByDate(
            List<Long> topicIds, LocalDate reflectionDate) {

        return reflectionRepository
                .findAllByTopicIdIsInAndReflectionDate(topicIds, reflectionDate)
                .stream()
                .map(
                        reflection ->
                                new ReflectionData(
                                        reflection.getId(),
                                        reflection.getMemberId(),
                                        reflection.getTopicId(),
                                        reflection.getContent(),
                                        reflection.getReflectionDate()))
                .toList();
    }
}
