package com.github.teamreflog.reflogserver.team.application.dto;

import com.github.teamreflog.reflogserver.team.domain.Crew;
import com.github.teamreflog.reflogserver.team.domain.Team;
import java.time.LocalDateTime;

public record CrewQueryResponse(
        String nickname, Long memberId, boolean isOwner, LocalDateTime joinedAt) {

    public static CrewQueryResponse fromEntity(final Crew crew, final Team team) {
        return new CrewQueryResponse(
                crew.getNickname(),
                crew.getMemberId(),
                team.isOwner(crew.getMemberId()),
                crew.getCreatedAt());
    }
}
