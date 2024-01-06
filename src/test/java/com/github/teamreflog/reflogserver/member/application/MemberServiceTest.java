package com.github.teamreflog.reflogserver.member.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.github.teamreflog.reflogserver.member.application.dto.MemberJoinRequest;
import com.github.teamreflog.reflogserver.member.domain.MemberEmail;
import com.github.teamreflog.reflogserver.member.domain.MemberRepository;
import com.github.teamreflog.reflogserver.member.domain.exception.EmailDuplicatedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@Sql(scripts = "/clear.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest
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
