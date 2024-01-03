package com.github.teamreflog.reflogserver.team.domain;

import java.util.List;
import org.springframework.data.repository.CrudRepository;

public interface CrewRepository extends CrudRepository<Crew, Long> {

    boolean existsByMemberIdAndTeamId(Long memberId, Long teamId);

    List<Crew> findAllByTeamId(Long teamId);

    boolean existsByTeamIdAndNickname(Long teamId, String nickname);
}
