package com.github.teamreflog.reflogserver.team.domain;

import org.springframework.data.repository.CrudRepository;

public interface TeamMemberRepository extends CrudRepository<TeamMember, Long> {

    boolean existsByMemberIdAndTeamId(Long memberId, Long teamId);
}
