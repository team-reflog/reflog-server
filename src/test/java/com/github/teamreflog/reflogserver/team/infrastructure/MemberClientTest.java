package com.github.teamreflog.reflogserver.team.infrastructure;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.github.teamreflog.reflogserver.common.exception.ReflogIllegalArgumentException;
import com.github.teamreflog.reflogserver.member.domain.MemberEmail;
import com.github.teamreflog.reflogserver.member.domain.MemberRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("단위 테스트: MemberClient")
class MemberClientTest {

    MemberClient memberClient;
    MemberRepository memberRepository;

    @BeforeEach
    void setUp() {
        this.memberRepository = mock(MemberRepository.class);
        this.memberClient = new MemberClient(memberRepository);
    }

    @Test
    @DisplayName("이메일에 해당하는 회원이 존재하지 않는다면 예외가 발생한다.")
    void throwExceptionNotExistEmailMember() {
        /* given */
        final String notExistEmail = "notExist@email.com";
        when(memberRepository.existsByEmail(new MemberEmail(notExistEmail))).thenReturn(false);

        /* when & then */
        assertThatCode(() -> memberClient.getIdByEmail(notExistEmail))
                .isExactlyInstanceOf(ReflogIllegalArgumentException.class);
    }
}
