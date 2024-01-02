package com.github.teamreflog.reflogserver.team.domain;

import java.util.List;
import org.springframework.data.repository.Repository;

public interface TeamInviteRepository extends Repository<TeamInvite, Long> {

    void save(TeamInvite teamInvite);

    List<TeamInvite> findAllByMemberId(Long memberId);

    boolean existsByMemberIdAndTeamId(Long memberId, Long teamId);
}
