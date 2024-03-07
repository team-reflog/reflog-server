package com.github.teamreflog.reflogserver.reflection.application;

import com.github.teamreflog.reflogserver.reflection.application.dto.ReflectionCreateRequest;
import com.github.teamreflog.reflogserver.reflection.application.dto.ReflectionDetailResponse;
import com.github.teamreflog.reflogserver.reflection.application.dto.ReflectionQueryResponse;
import com.github.teamreflog.reflogserver.reflection.application.dto.ReflectionTodayInTeamQueryResponse;
import com.github.teamreflog.reflogserver.reflection.application.dto.ReflectionTodayQueryRequest;
import com.github.teamreflog.reflogserver.reflection.domain.CrewData;
import com.github.teamreflog.reflogserver.reflection.domain.DateProvider;
import com.github.teamreflog.reflogserver.reflection.domain.Reflection;
import com.github.teamreflog.reflogserver.reflection.domain.ReflectionRepository;
import com.github.teamreflog.reflogserver.reflection.domain.TeamData;
import com.github.teamreflog.reflogserver.reflection.domain.TeamQueryService;
import com.github.teamreflog.reflogserver.reflection.domain.TopicData;
import com.github.teamreflog.reflogserver.reflection.domain.TopicQueryService;
import com.github.teamreflog.reflogserver.reflection.infrastructure.CrewService;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReflectionService {

    private final ReflectionRepository reflectionRepository;
    private final DateProvider dateProvider;
    private final TopicQueryService topicQueryService;
    private final TeamQueryService teamQueryService;
    private final CrewService crewService;

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

    @Transactional(readOnly = true)
    public ReflectionTodayInTeamQueryResponse queryTodayTeamReflections(final Long teamId) {
        final LocalDate today = LocalDate.now();

        final TeamData team = teamQueryService.getTeamDataById(teamId);

        final List<Long> topicIds =
                topicQueryService.getAllTopicDataByTeamId(team.teamId()).stream()
                        .map(TopicData::topicId)
                        .toList();

        final List<ReflectionDetailResponse> reflectionDetails =
                matchReflectionWithWriter(
                        teamId,
                        reflectionRepository.findAllByTopicIdIsInAndReflectionDate(
                                topicIds, today));

        return ReflectionTodayInTeamQueryResponse.fromEntity(team, reflectionDetails);
    }

    private List<ReflectionDetailResponse> matchReflectionWithWriter(
            final Long teamId, final List<Reflection> reflections) {
        final List<Long> reflectionWroteCrewIds =
                reflections.stream().map(Reflection::getMemberId).toList();

        final Map<Long, CrewData> crewsByMemberId =
                crewService
                        .getCrewDatasByMemberIdIsInAndTeamId(reflectionWroteCrewIds, teamId)
                        .stream()
                        .collect(Collectors.toMap(CrewData::memberId, Function.identity()));

        return reflections.stream()
                .map(
                        reflection ->
                                ReflectionDetailResponse.fromEntity(
                                        crewsByMemberId.get(reflection.getMemberId()), reflection))
                .toList();
    }
}
