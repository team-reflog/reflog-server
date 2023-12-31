package com.github.teamreflog.reflogserver.member.service;

import static org.assertj.core.api.Assertions.assertThat;

import com.github.teamreflog.reflogserver.config.JpaConfig;
import com.github.teamreflog.reflogserver.member.domain.MemberRepository;
import com.github.teamreflog.reflogserver.member.dto.MemberJoinRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

@DataJpaTest
@Import({MemberService.class, JpaConfig.class})
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
        assertThat(memberRepository.findByEmail(request.email())).isPresent();
    }
}
