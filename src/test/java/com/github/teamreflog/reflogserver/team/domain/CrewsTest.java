package com.github.teamreflog.reflogserver.team.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.github.teamreflog.reflogserver.team.domain.exception.CrewAlreadyJoinedException;
import com.github.teamreflog.reflogserver.team.domain.exception.NicknameDuplicateException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("단위 테스트: Crews")
class CrewsTest {

    @Test
    @DisplayName("팀원을 추가한다.")
    void addCrew() {
        /* given */
        final Crews crews = Crews.from(List.of());

        /* when */
        final Crews newCrews = crews.add(Crew.of(1L, 1L, "super-duper"));

        /* then */
        assertThat(newCrews.getCrews())
                .extracting(Crew::getNickname)
                .containsExactly("super-duper");
    }

    @Test
    @DisplayName("닉네임이 중복되는 경우 예외가 발생한다.")
    void throwExceptionWithDuplicatedNickname() {
        /* given */
        final Crews crews = Crews.from(List.of(Crew.of(1L, 1L, "super-duper")));

        final Crew duplicateNickname = Crew.of(1L, 2L, "super-duper");

        /* when & then */
        assertThatCode(() -> crews.add(duplicateNickname))
                .isExactlyInstanceOf(NicknameDuplicateException.class)
                .hasMessage("팀 내에 이미 같은 이름이 있습니다.");
    }

    @Test
    @DisplayName("회원 아이디가 중복되는 경우 예외가 발생한다.")
    void a() {
        /* given */
        final Crews crews = Crews.from(List.of(Crew.of(1L, 1L, "super-duper")));

        final Crew duplicateMember = Crew.of(1L, 1L, "duper-duper");

        /* when & then */
        assertThatCode(() -> crews.add(duplicateMember))
                .isExactlyInstanceOf(CrewAlreadyJoinedException.class)
                .hasMessage("이미 팀원입니다.");
    }
}
