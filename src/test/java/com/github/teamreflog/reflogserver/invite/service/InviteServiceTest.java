package com.github.teamreflog.reflogserver.invite.service;

import static org.assertj.core.api.Assertions.assertThatCode;

import com.github.teamreflog.reflogserver.auth.dto.AuthPrincipal;
import com.github.teamreflog.reflogserver.common.config.JpaConfig;
import com.github.teamreflog.reflogserver.invite.dto.InviteAcceptRequest;
import com.github.teamreflog.reflogserver.invite.exception.InviteNotExistException;
import com.github.teamreflog.reflogserver.invite.exception.UnauthorizedInviteException;
import com.github.teamreflog.reflogserver.team.exception.NicknameDuplicateException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Import({InviteService.class, JpaConfig.class})
@DisplayName("통합 테스트: InviteService")
class InviteServiceTest {

    @Autowired InviteService inviteService;

    @Nested
    @DisplayName("초대 수락 테스트")
    class AcceptInviteTest {

        @Test
        @DisplayName("존재하지 않는 초대인 경우 예외가 발생한다.")
        void inviteNotExistThrowsException() {
            /* given */
            final AuthPrincipal authPrincipal = new AuthPrincipal(2L);
            final InviteAcceptRequest request = new InviteAcceptRequest("owner");

            /* when, then */
            assertThatCode(() -> inviteService.acceptInvite(authPrincipal, 4L, request))
                    .isExactlyInstanceOf(InviteNotExistException.class);
        }

        @Test
        @DisplayName("초대 대상 사용자와 요청 사용자 토큰이 일치하지 않는 경우 예외가 발생한다.")
        @Sql("/invite.sql")
        void userTokenInvalidThrowsException() {
            /* given */
            final AuthPrincipal authPrincipal = new AuthPrincipal(777L);
            final InviteAcceptRequest request = new InviteAcceptRequest("super-duper-nickname");

            /* when & then */
            assertThatCode(() -> inviteService.acceptInvite(authPrincipal, 1L, request))
                    .isExactlyInstanceOf(UnauthorizedInviteException.class);
        }

        @Test
        @DisplayName("팀 내 닉네임 중복 시 예외가 발생한다.")
        @Sql({"/member.sql", "/team.sql", "/team_member.sql", "/invite.sql"})
        void duplicateNicknameInTeamThrowsException() {
            /* given */
            final AuthPrincipal authPrincipal = new AuthPrincipal(2L);
            final InviteAcceptRequest request = new InviteAcceptRequest("owner");

            /* when */
            assertThatCode(() -> inviteService.acceptInvite(authPrincipal, 1L, request))
                    .isInstanceOf(NicknameDuplicateException.class);
        }
    }
}
