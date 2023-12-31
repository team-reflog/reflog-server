package com.github.teamreflog.reflogserver.member.service;

import com.github.teamreflog.reflogserver.member.domain.MemberRepository;
import com.github.teamreflog.reflogserver.member.dto.MemberJoinRequest;
import com.github.teamreflog.reflogserver.member.exception.EmailDuplicatedException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    public void createMember(final MemberJoinRequest request) {
        if (memberRepository.findByEmail(request.email()).isPresent()) {
            throw new EmailDuplicatedException();
        }

        memberRepository.save(request.toEntity());
    }
}
