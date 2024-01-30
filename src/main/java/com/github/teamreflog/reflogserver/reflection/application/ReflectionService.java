package com.github.teamreflog.reflogserver.reflection.application;

import com.github.teamreflog.reflogserver.reflection.application.dto.ReflectionCreateRequest;
import com.github.teamreflog.reflogserver.reflection.application.dto.ReflectionQueryResponse;
import com.github.teamreflog.reflogserver.reflection.application.dto.ReflectionTodayQueryRequest;
import com.github.teamreflog.reflogserver.reflection.domain.DateProvider;
import com.github.teamreflog.reflogserver.reflection.domain.ReflectionRepository;
import com.github.teamreflog.reflogserver.reflection.domain.TopicQueryService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReflectionService {

    private final ReflectionRepository reflectionRepository;
    private final DateProvider dateProvider;
    private final TopicQueryService topicQueryService;

    // TODO: 회고일에만 회고를 작성할 수 있다.
    @Transactional
    public Long createReflection(final ReflectionCreateRequest request) {
        final LocalDate localDate = dateProvider.getTodayOfZone(request.timezone());

        return reflectionRepository.save(request.toEntity(localDate)).getId();
    }

    public List<ReflectionQueryResponse> queryTodayReflections(
            final ReflectionTodayQueryRequest request) {

        return reflectionRepository
                .findAllByMemberIdAndReflectionDate(
                        request.memberId(), dateProvider.getTodayOfZone(request.timezone()))
                .stream()
                .map(
                        reflection ->
                                ReflectionQueryResponse.fromEntity(
                                        reflection,
                                        topicQueryService.getTopicDataById(
                                                reflection.getTopicId())))
                .toList();
    }
}
