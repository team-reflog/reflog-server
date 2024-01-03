package com.github.teamreflog.reflogserver.team.application;

import static org.assertj.core.api.Assertions.assertThatCode;

import com.github.teamreflog.reflogserver.auth.dto.AuthPrincipal;
import com.github.teamreflog.reflogserver.common.config.JpaConfig;
import com.github.teamreflog.reflogserver.team.application.dto.TeamCreateRequest;
import com.github.teamreflog.reflogserver.team.domain.exception.TeamNameDuplicatedException;
import com.github.teamreflog.reflogserver.team.domain.exception.TeamReflectionDaysEmptyException;
import java.time.DayOfWeek;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({TeamService.class, JpaConfig.class})
@DisplayName("통합 테스트: TeamService")
class TeamServiceTest {

    @Autowired TeamService teamService;

    @Test
    @DisplayName("팀을 생성한다.")
    void createTeam() {
        /* given */
        final AuthPrincipal authPrincipal = new AuthPrincipal(1L);
        final TeamCreateRequest request =
                new TeamCreateRequest(
                        "antifragile",
                        "안티프래질 팀입니다.",
                        "owner",
                        List.of(
                                DayOfWeek.MONDAY,
                                DayOfWeek.WEDNESDAY,
                                DayOfWeek.FRIDAY,
                                DayOfWeek.SUNDAY));

        /* when & then */
        assertThatCode(() -> teamService.createTeam(authPrincipal, request))
                .doesNotThrowAnyException();
    }

    @Test
    @DisplayName("팀 이름이 중복되면 예외가 발생한다.")
    void throwExceptionWithDuplicatedTeamName() {
        /* given */
        final AuthPrincipal authPrincipal = new AuthPrincipal(1L);
        final TeamCreateRequest request =
                new TeamCreateRequest(
                        "antifragile",
                        "안티프래질 팀입니다.",
                        "owner",
                        List.of(
                                DayOfWeek.MONDAY,
                                DayOfWeek.WEDNESDAY,
                                DayOfWeek.FRIDAY,
                                DayOfWeek.SUNDAY));

        /* when */
        teamService.createTeam(authPrincipal, request);

        /* then */
        assertThatCode(() -> teamService.createTeam(authPrincipal, request))
                .isExactlyInstanceOf(TeamNameDuplicatedException.class)
                .hasMessage("이미 사용중인 팀 이름입니다.");
    }

    @Test
    @DisplayName("회고일이 비어있는 경우 예외가 발생한다.")
    void throwExceptionWithEmptyReflectionDays() {
        /* given */
        final AuthPrincipal authPrincipal = new AuthPrincipal(1L);
        final TeamCreateRequest request =
                new TeamCreateRequest("antifragile", "안티프래질 팀입니다.", "owner", List.of());

        /* when & then */
        assertThatCode(() -> teamService.createTeam(authPrincipal, request))
                .isExactlyInstanceOf(TeamReflectionDaysEmptyException.class)
                .hasMessage("회고일은 최소 하루 이상이어야 합니다.");
    }
}
