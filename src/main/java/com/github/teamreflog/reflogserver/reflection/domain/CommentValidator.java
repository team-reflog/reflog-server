package com.github.teamreflog.reflogserver.reflection.domain;

import com.github.teamreflog.reflogserver.common.exception.ReflogIllegalArgumentException;
import com.github.teamreflog.reflogserver.reflection.exception.CommentNotAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentValidator {

    private final CrewQueryService crewQueryService;

    public void validateAccess(final Long memberId, final Long reflectionId) {
        try {
            crewQueryService.getCrewDataByMemberIdAndReflectionId(memberId, reflectionId);
        } catch (final ReflogIllegalArgumentException e) {
            throw new CommentNotAccessException(e);
        }
    }
}
