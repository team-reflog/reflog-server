package com.github.teamreflog.reflogserver.reflection.application;

import com.github.teamreflog.reflogserver.reflection.application.dto.ReflectionCreateRequest;
import com.github.teamreflog.reflogserver.reflection.application.dto.ReflectionQueryResponse;
import com.github.teamreflog.reflogserver.reflection.application.dto.ReflectionTodayQueryRequest;
import com.github.teamreflog.reflogserver.reflection.domain.DateProvider;
import com.github.teamreflog.reflogserver.reflection.domain.DateRange;
import com.github.teamreflog.reflogserver.reflection.domain.ReflectionRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReflectionService {

    private final ReflectionRepository reflectionRepository;
    private final DateProvider dateProvider;

    @Transactional
    public Long createReflection(final ReflectionCreateRequest request) {
        return reflectionRepository.save(request.toEntity()).getId();
    }

    public List<ReflectionQueryResponse> queryTodayReflections(
            final ReflectionTodayQueryRequest request) {
        final DateRange range = dateProvider.getTodayRange(request.timezone());

        return reflectionRepository
                .findAllByMemberIdAndCreatedAtBetween(
                        request.memberId(), range.start(), range.end())
                .stream()
                .map(ReflectionQueryResponse::fromEntity)
                .toList();
    }
}
