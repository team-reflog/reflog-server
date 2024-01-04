package com.github.teamreflog.reflogserver.team.domain;

import java.time.DayOfWeek;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;

@DisplayName("단위 테스트: Team")
class TeamTest {

    Team team;

    @BeforeEach
    void setUp() {
        team =
                Team.of(
                        "antifragile",
                        "안티프레질 팀입니다.",
                        777L,
                        List.of(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY));
    }

    // TODO: check later
    //    @Test
    //    @DisplayName("초대 처리 테스트")
    //    void processInvite() {
    //        /* given */
    //        final Invite invite = Invite.of(team, 123L);
    //
    //        /* when */
    //        team.processInvite(invite, "super-duper");
    //
    //        /* then */
    //        assertAll(
    //                () ->
    //                        assertThat(team.getCrews())
    //                                .extracting(Crew::getNickname)
    //                                .containsExactly("super-duper"),
    //                () -> assertThat(team.getInvites()).isEmpty());
    //    }

    //    @Test
    //    @DisplayName("닉네임이 중복되는 경우 예외가 발생한다.")
    //    void throwExceptionWithDuplicatedNickname() {
    //        /* given */
    //        final Invite invite1 = Invite.of(team, 1L);
    //        team.processInvite(invite1, "super-duper");
    //
    //        final Invite invite2 = Invite.of(team, 2L);
    //
    //        /* when & then */
    //        assertThatCode(() -> team.processInvite(invite2, "super-duper"))
    //                .isExactlyInstanceOf(NicknameDuplicateException.class)
    //                .hasMessage("팀 내에 이미 같은 이름이 있습니다.");
    //    }

    //    @Test
    //    @DisplayName("회원 아이디가 중복되는 경우 예외가 발생한다.")
    //    void a() {
    //        /* given */
    //        final Invite invite1 = Invite.of(team, 1L);
    //        team.processInvite(invite1, "super-duper");
    //
    //        final Invite invite2 = Invite.of(team, 1L);
    //
    //        /* when & then */
    //        assertThatCode(() -> team.processInvite(invite1, "duper-super"))
    //                .isExactlyInstanceOf(CrewAlreadyJoinedException.class)
    //                .hasMessage("이미 팀원입니다.");
    //    }
}
