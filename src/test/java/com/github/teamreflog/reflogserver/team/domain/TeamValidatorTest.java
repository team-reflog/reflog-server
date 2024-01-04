package com.github.teamreflog.reflogserver.team.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.github.teamreflog.reflogserver.team.domain.exception.TeamNameDuplicatedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("단위 테스트: TeamValidator")
class TeamValidatorTest {

    TeamValidator teamValidator;
    TeamRepository teamRepository;

    @BeforeEach
    void setUp() {
        teamRepository = mock(TeamRepository.class);
        teamValidator = new TeamValidator(teamRepository);
    }

    @Test
    @DisplayName("팀 이름이 중복되면 예외가 발생한다.")
    void throwExceptionWithDuplicatedTeamName() {
        /* given */
        String existingName = "name";
        when(teamRepository.existsByName(existingName)).thenReturn(true);

        /* when & then */
        assertThatCode(() -> teamValidator.validateDuplicateTeamName(existingName))
                .isExactlyInstanceOf(TeamNameDuplicatedException.class)
                .hasMessage("이미 사용중인 팀 이름입니다.");
    }
}
