package com.github.teamreflog.reflogserver.team.domain;

import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface TeamRepository extends Repository<Team, Long> {

    Team save(Team team);

    boolean existsByName(String name);

    Optional<Team> findById(Long teamId);
}
