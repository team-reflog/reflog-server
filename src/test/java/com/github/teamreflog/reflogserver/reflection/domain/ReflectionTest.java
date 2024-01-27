package com.github.teamreflog.reflogserver.reflection.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.github.teamreflog.reflogserver.reflection.exception.ReflectionAlreadyExistException;
import java.time.LocalDate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("단위 테스트: Reflection")
class ReflectionTest {

    @Nested
    @DisplayName("회고를 생성할 때")
    class CreateReflection {

        @Nested
        @DisplayName("회고 일이 아니라면")
        class NotReflectionDate {

            TeamData teamData = mock(TeamData.class);

            @BeforeEach
            void setUp() {
                given(teamData.containsReflectionDay(any())).willReturn(false);
            }

            @Test
            @DisplayName("예외가 발생한다.")
            void throwException() {
                assertThatCode(
                                () ->
                                        Reflection.create(
                                                1L, 1L, "content", mock(LocalDate.class), teamData))
                        .isExactlyInstanceOf(ReflectionAlreadyExistException.class)
                        .hasMessage("이미 오늘의 회고를 작성했습니다.");
            }
        }
    }
}
