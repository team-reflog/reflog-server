package com.github.teamreflog.reflogserver.team.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface InviteRepository extends Repository<Invite, Long> {

    List<Invite> findAllByMemberId(Long memberId);

    Optional<Invite> findById(Long id);

    void delete(Invite invite);

    void save(Invite invite);
}
