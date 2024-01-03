package com.github.teamreflog.reflogserver.member.service;

import com.github.teamreflog.reflogserver.member.domain.Member;
import com.github.teamreflog.reflogserver.member.domain.MemberEmail;
import com.github.teamreflog.reflogserver.member.domain.MemberRepository;
import com.github.teamreflog.reflogserver.member.dto.MemberJoinRequest;
import com.github.teamreflog.reflogserver.member.exception.EmailDuplicatedException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    public Long createMember(final MemberJoinRequest request) {
        if (memberRepository.existsByEmail(new MemberEmail(request.email()))) {
            throw new EmailDuplicatedException();
        }

        return memberRepository
                .save(Member.of(request.email(), passwordEncoder.encode(request.password())))
                .getId();
    }
}
