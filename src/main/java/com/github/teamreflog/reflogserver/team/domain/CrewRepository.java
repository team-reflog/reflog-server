package com.github.teamreflog.reflogserver.team.domain;

import java.util.List;
import org.springframework.data.repository.Repository;

public interface CrewRepository extends Repository<Crew, Long> {

    List<Crew> findAllByTeamId(Long teamId);

    void save(Crew of);

    List<Crew> findAllByMemberId(Long memberId);
}
