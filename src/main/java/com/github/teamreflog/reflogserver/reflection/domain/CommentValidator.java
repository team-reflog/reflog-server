package com.github.teamreflog.reflogserver.reflection.domain;

import com.github.teamreflog.reflogserver.common.exception.ReflogIllegalArgumentException;
import com.github.teamreflog.reflogserver.reflection.exception.CommentNotAccessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommentValidator {

    private final CrewQueryClient crewQueryClient;

    public void validateAccess(final Long memberId, final Long reflectionId) {
        try {
            crewQueryClient.getCrewDataByMemberIdAndReflectionId(memberId, reflectionId);
        } catch (final ReflogIllegalArgumentException e) {
            throw new CommentNotAccessException(e);
        }
    }
}
