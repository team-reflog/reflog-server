package com.github.teamreflog.reflogserver.reflection.application;

import com.github.teamreflog.reflogserver.reflection.application.dto.ReflectionCreateRequest;
import com.github.teamreflog.reflogserver.reflection.application.dto.ReflectionQueryResponse;
import com.github.teamreflog.reflogserver.reflection.application.dto.ReflectionTodayQueryRequest;
import com.github.teamreflog.reflogserver.reflection.domain.DateProvider;
import com.github.teamreflog.reflogserver.reflection.domain.Reflection;
import com.github.teamreflog.reflogserver.reflection.domain.ReflectionRepository;
import com.github.teamreflog.reflogserver.reflection.domain.ReflectionValidator;
import com.github.teamreflog.reflogserver.reflection.domain.TeamQueryService;
import java.time.LocalDate;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReflectionService {

    private final ReflectionRepository reflectionRepository;
    private final ReflectionValidator reflectionValidator;
    private final DateProvider dateProvider;
    private final TeamQueryService teamQueryService;

    @Transactional
    public Long createReflection(final ReflectionCreateRequest request) {
        final LocalDate now = dateProvider.getLocalDateNow(request.timezone());

        reflectionValidator.validateReflectionExistence(request.memberId(), request.topicId(), now);

        return reflectionRepository
                .save(
                        Reflection.create(
                                request.memberId(),
                                request.topicId(),
                                request.content(),
                                now,
                                teamQueryService.getTeamDataByTopicId(request.topicId())))
                .getId();
    }

    @Transactional(readOnly = true)
    public List<ReflectionQueryResponse> queryTodayReflections(
            final ReflectionTodayQueryRequest request) {

        return reflectionRepository
                .findAllByMemberIdAndReflectionDate(
                        request.memberId(), dateProvider.getLocalDateNow(request.timezone()))
                .stream()
                .map(ReflectionQueryResponse::fromEntity)
                .toList();
    }
}
