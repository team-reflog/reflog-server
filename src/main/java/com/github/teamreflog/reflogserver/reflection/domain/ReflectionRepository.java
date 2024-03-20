package com.github.teamreflog.reflogserver.reflection.domain;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface ReflectionRepository extends Repository<Reflection, Long> {

    Reflection save(Reflection reflection);

    List<Reflection> findAllByMemberIdAndReflectionDate(Long memberId, LocalDate reflectionDate);

    Optional<Reflection> findById(Long id);

    Optional<Reflection> findByMemberIdAndTopicIdAndReflectionDate(
            Long memberId, Long topicId, LocalDate reflectionDate);
}
