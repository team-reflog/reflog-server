package com.github.teamreflog.reflogserver.team.dto;

import com.github.teamreflog.reflogserver.team.domain.Team;
import com.github.teamreflog.reflogserver.team.domain.TeamMember;
import java.time.LocalDateTime;

public record TeamMemberQueryResponse(
        String nickname, Long memberId, boolean isOwner, LocalDateTime joinedAt) {

    public static TeamMemberQueryResponse fromEntity(final TeamMember teamMember, final Team team) {
        return new TeamMemberQueryResponse(
                teamMember.getNickname(),
                teamMember.getMemberId(),
                team.isOwner(teamMember.getMemberId()),
                teamMember.getCreatedAt());
    }
}
