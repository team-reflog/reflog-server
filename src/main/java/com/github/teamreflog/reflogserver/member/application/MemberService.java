package com.github.teamreflog.reflogserver.member.application;

import com.github.teamreflog.reflogserver.member.application.dto.MemberJoinRequest;
import com.github.teamreflog.reflogserver.member.domain.Member;
import com.github.teamreflog.reflogserver.member.domain.MemberRepository;
import com.github.teamreflog.reflogserver.member.domain.MemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberValidator memberValidator;
    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    public Long createMember(final MemberJoinRequest request) {
        memberValidator.validateEmailDuplicated(request.email());

        return memberRepository
                .save(Member.of(request.email(), passwordEncoder.encode(request.password())))
                .getId();
    }
}
