package com.github.teamreflog.reflogserver.invite.service;

import static org.assertj.core.api.Assertions.assertThatCode;

import com.github.teamreflog.reflogserver.common.config.JpaConfig;
import com.github.teamreflog.reflogserver.team.application.InviteService;
import com.github.teamreflog.reflogserver.team.application.dto.InviteAcceptRequest;
import com.github.teamreflog.reflogserver.team.domain.InviteValidator;
import com.github.teamreflog.reflogserver.team.domain.exception.InviteNotExistException;
import com.github.teamreflog.reflogserver.team.infrastructure.MemberClient;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({InviteService.class, JpaConfig.class, InviteValidator.class, MemberClient.class})
@DisplayName("통합 테스트: InviteService")
class InviteServiceTest {

    @Autowired InviteService inviteService;

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
    }

    @Nested
    @DisplayName("초대를 거절할 때")
    class RejectInviteTest {

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
    }
}
