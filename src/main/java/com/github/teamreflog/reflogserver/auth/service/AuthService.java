package com.github.teamreflog.reflogserver.auth.service;

import com.github.teamreflog.reflogserver.auth.dto.LoginRequest;
import com.github.teamreflog.reflogserver.auth.exception.EmailNotExistException;
import com.github.teamreflog.reflogserver.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;

    public void login(final LoginRequest request) {
        memberRepository
                .findByEmail(request.getMemberEmail())
                .orElseThrow(EmailNotExistException::new);
    }
}
