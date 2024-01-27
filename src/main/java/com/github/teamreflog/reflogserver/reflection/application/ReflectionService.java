package com.github.teamreflog.reflogserver.reflection.application;

import com.github.teamreflog.reflogserver.reflection.application.dto.ReflectionCreateRequest;
import com.github.teamreflog.reflogserver.reflection.application.dto.ReflectionQueryResponse;
import com.github.teamreflog.reflogserver.reflection.application.dto.ReflectionTodayQueryRequest;
import com.github.teamreflog.reflogserver.reflection.domain.DateProvider;
import com.github.teamreflog.reflogserver.reflection.domain.Reflection;
import com.github.teamreflog.reflogserver.reflection.domain.ReflectionRepository;
import com.github.teamreflog.reflogserver.reflection.domain.TeamData;
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
    private final DateProvider dateProvider;
    private final TeamQueryService teamQueryService;

    // TODO: 동일 주제에 대하여 중복 회고를 작성할 수 없음
    @Transactional
    public Long createReflection(final ReflectionCreateRequest request) {
        final LocalDate localDate = dateProvider.getTodayOfZone(request.timezone());

        final TeamData teamData = teamQueryService.getTeamDataByTopicId(request.topicId());

        final Reflection reflection =
                Reflection.create(
                        request.memberId(),
                        request.topicId(),
                        request.content(),
                        localDate,
                        teamData);

        return reflectionRepository.save(reflection).getId();
    }

    @Transactional(readOnly = true)
    public List<ReflectionQueryResponse> queryTodayReflections(
            final ReflectionTodayQueryRequest request) {

        return reflectionRepository
                .findAllByMemberIdAndReflectionDate(
                        request.memberId(), dateProvider.getTodayOfZone(request.timezone()))
                .stream()
                .map(ReflectionQueryResponse::fromEntity)
                .toList();
    }
}
