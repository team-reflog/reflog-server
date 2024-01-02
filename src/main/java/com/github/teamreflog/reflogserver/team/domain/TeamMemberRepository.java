package com.github.teamreflog.reflogserver.team.domain;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface TeamMemberRepository extends CrudRepository<TeamMember, Long> {

    boolean existsByMemberIdAndTeamId(Long memberId, Long teamId);

    List<TeamMember> findAllByTeamId(Long teamId);
}
