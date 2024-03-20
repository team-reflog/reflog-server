package com.github.teamreflog.reflogserver.reflection.domain;

import com.github.teamreflog.reflogserver.reflection.exception.ReflectionAlreadyExistException;
import java.time.LocalDate;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ReflectionValidator {

    private final ReflectionRepository reflectionRepository;

    public void validateReflectionExistence(
            final Long memberId, final Long topicId, final LocalDate reflectionDate) {
        validateReflectionExistence(
                reflectionRepository.findByMemberIdAndTopicIdAndReflectionDate(
                        memberId, topicId, reflectionDate));
    }

    void validateReflectionExistence(final Optional<Reflection> reflection) {
        reflection.ifPresent(
                ignore -> {
                    throw new ReflectionAlreadyExistException();
                });
    }
}
