package com.github.teamreflog.reflogserver.team.domain;

import static org.assertj.core.api.Assertions.assertThatCode;

import com.github.teamreflog.reflogserver.team.domain.exception.TeamReflectionDaysEmptyException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("단위 테스트: Team")
class TeamTest {

    @Test
    @DisplayName("회고일이 비어있는 경우 예외가 발생한다.")
    void throwExceptionWithEmptyReflectionDays() {
        /* given */

        /* when & then */
        assertThatCode(() -> Team.of("name", "description", 1L, List.of()))
                .isExactlyInstanceOf(TeamReflectionDaysEmptyException.class)
                .hasMessage("회고일은 최소 하루 이상이어야 합니다.");
    }
}
