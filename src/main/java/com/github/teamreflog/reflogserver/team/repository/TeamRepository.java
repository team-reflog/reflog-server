package com.github.teamreflog.reflogserver.team.repository;

import com.github.teamreflog.reflogserver.team.domain.Team;
import org.springframework.data.repository.Repository;

public interface TeamRepository extends Repository<Team, Long> {

    void save(Team team);

    boolean existsByName(String name);
}
