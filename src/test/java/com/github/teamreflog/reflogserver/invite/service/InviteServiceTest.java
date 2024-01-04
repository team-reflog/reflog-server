package com.github.teamreflog.reflogserver.invite.service;

import static org.assertj.core.api.Assertions.assertThatCode;

import com.github.teamreflog.reflogserver.auth.application.dto.AuthPrincipal;
import com.github.teamreflog.reflogserver.common.config.JpaConfig;
import com.github.teamreflog.reflogserver.member.domain.exception.MemberNotExistException;
import com.github.teamreflog.reflogserver.team.application.InviteService;
import com.github.teamreflog.reflogserver.team.application.dto.InviteAcceptRequest;
import com.github.teamreflog.reflogserver.team.application.dto.InviteCreateRequest;
import com.github.teamreflog.reflogserver.team.domain.exception.InviteNotExistException;
import com.github.teamreflog.reflogserver.team.domain.exception.UnauthorizedInviteException;
import com.github.teamreflog.reflogserver.topic.domain.exception.NotOwnerException;
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
    @DisplayName("초대를 할 때")
    class InviteCrewTest {

        @Test
        @DisplayName("팀장이 아닌 경우 예외가 발생한다.")
        @Sql("/team.sql")
        void throwExceptionNotOwner() {
            /* given */
            final AuthPrincipal notOwnerPrincipal = new AuthPrincipal(777L);
            final InviteCreateRequest request = new InviteCreateRequest("crew@email.com", 1L);

            /* when & then */
            assertThatCode(() -> inviteService.inviteCrew(notOwnerPrincipal, request))
                    .isExactlyInstanceOf(NotOwnerException.class);
        }

        @Test
        @DisplayName("이메일에 해당하는 회원이 존재하지 않는다면 예외가 발생한다.")
        @Sql("/team.sql")
        void throwExceptionNotExistEmailMember() {
            /* given */
            final AuthPrincipal ownerPrincipal = new AuthPrincipal(1L);
            final InviteCreateRequest request = new InviteCreateRequest("notExist@email.com", 1L);

            /* when & then */
            assertThatCode(() -> inviteService.inviteCrew(ownerPrincipal, request))
                    .isExactlyInstanceOf(MemberNotExistException.class);
        }
    }

    @Nested
    @DisplayName("초대를 수락할 때")
    class AcceptInviteTest {

        @Test
        @DisplayName("존재하지 않는 초대인 경우 예외가 발생한다.")
        void inviteNotExistThrowsException() {
            /* given */
            final InviteAcceptRequest request =
                    new InviteAcceptRequest(2L, 4L, "super-duper-nickname");

            /* when, then */
            assertThatCode(() -> inviteService.acceptInvite(request))
                    .isExactlyInstanceOf(InviteNotExistException.class);
        }

        @Test
        @DisplayName("초대 대상 사용자와 요청 사용자 토큰이 일치하지 않는 경우 예외가 발생한다.")
        @Sql({"/invite.sql", "/team.sql"})
        void userTokenInvalidThrowsException() {
            /* given */
            final InviteAcceptRequest request =
                    new InviteAcceptRequest(777L, 1L, "super-duper-nickname");

            /* when & then */
            assertThatCode(() -> inviteService.acceptInvite(request))
                    .isExactlyInstanceOf(UnauthorizedInviteException.class);
        }
    }
}
