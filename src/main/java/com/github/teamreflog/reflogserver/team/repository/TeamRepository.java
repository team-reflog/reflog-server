package com.github.teamreflog.reflogserver.team.repository;

import com.github.teamreflog.reflogserver.team.domain.Team;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface TeamRepository extends Repository<Team, Long> {

    Team save(Team team);

    boolean existsByName(String name);

    boolean existsByIdAndOwnerId(Long teamId, Long ownerId);

    Optional<Team> findById(Long teamId);
}
