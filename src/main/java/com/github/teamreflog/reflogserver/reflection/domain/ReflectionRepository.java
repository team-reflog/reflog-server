package com.github.teamreflog.reflogserver.reflection.domain;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.repository.Repository;

public interface ReflectionRepository extends Repository<Reflection, Long> {

    Reflection save(Reflection reflection);

    List<Reflection> findAllByMemberIdAndCreatedAtBetween(
            Long memberId, LocalDateTime start, LocalDateTime end);
}
