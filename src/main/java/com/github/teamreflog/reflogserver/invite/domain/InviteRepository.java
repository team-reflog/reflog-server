package com.github.teamreflog.reflogserver.invite.domain;

import java.util.List;
import org.springframework.data.repository.Repository;

public interface InviteRepository extends Repository<Invite, Long> {

    void save(Invite invite);

    List<Invite> findAllByMemberId(Long memberId);

    boolean existsByMemberIdAndTeamId(Long memberId, Long teamId);
}
