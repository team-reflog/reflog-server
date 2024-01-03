package com.github.teamreflog.reflogserver.member.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.github.teamreflog.reflogserver.auth.config.AuthConfig;
import com.github.teamreflog.reflogserver.common.config.JpaConfig;
import com.github.teamreflog.reflogserver.member.domain.MemberEmail;
import com.github.teamreflog.reflogserver.member.domain.MemberRepository;
import com.github.teamreflog.reflogserver.member.dto.MemberJoinRequest;
import com.github.teamreflog.reflogserver.member.exception.EmailDuplicatedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({MemberService.class, AuthConfig.class, JpaConfig.class})
@DisplayName("통합 테스트: MemberService")
class MemberServiceTest {

    @Autowired MemberService memberService;

    @Autowired MemberRepository memberRepository;

    @Test
    @DisplayName("회원 가입 테스트")
    void createMember() {
        /* given */
        final MemberJoinRequest request = new MemberJoinRequest("reflog@email.com", "reflog");

        /* when */
        memberService.createMember(request);

        /* then */
        assertThat(memberRepository.existsByEmail(new MemberEmail(request.email()))).isTrue();
    }

    @Test
    @DisplayName("회원 가입 시 중복된 이메일을 사용하는 경우 실패한다.")
    void createMemberFailWithDuplicatedEmail() {
        /* given */
        final MemberJoinRequest request = new MemberJoinRequest("reflog@email.com", "reflog");

        /* when */
        memberService.createMember(request);

        /* then */
        assertThatCode(() -> memberService.createMember(request))
                .isExactlyInstanceOf(EmailDuplicatedException.class)
                .hasMessage("이미 사용중인 이메일입니다.");
    }
}
