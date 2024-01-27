package com.github.teamreflog.reflogserver.reflection.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.mock;

import com.github.teamreflog.reflogserver.reflection.exception.ReflectionAlreadyExistException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("단위 테스트: ReflectionValidator")
class ReflectionValidatorTest {

    ReflectionValidator reflectionValidator;

    @BeforeEach
    void setUp() {
        reflectionValidator = new ReflectionValidator(mock(ReflectionRepository.class));
    }

    @Nested
    @DisplayName("회고가 이미 존재하는지 검사할 때")
    class ValidateReflectionExistence {

        @Nested
        @DisplayName("회고가 존재하지 않으면")
        class ReflectionNotExist {

            @Test
            @DisplayName("예외를 발생시키지 않는다.")
            void notThrowException() {
                /* given */
                final Optional<Reflection> reflection = Optional.empty();

                /* when & then */
                assertThatCode(() -> reflectionValidator.validateReflectionExistence(reflection))
                        .doesNotThrowAnyException();
            }
        }

        @Nested
        @DisplayName("회고가 존재하면")
        class ReflectionExist {

            @Test
            @DisplayName("ReflectionAlreadyExistException 예외를 발생시킨다.")
            void throwException() {
                /* given */
                final Optional<Reflection> reflection = Optional.of(mock(Reflection.class));

                /* when & then */
                assertThatCode(() -> reflectionValidator.validateReflectionExistence(reflection))
                        .isInstanceOf(ReflectionAlreadyExistException.class);
            }
        }
    }
}
