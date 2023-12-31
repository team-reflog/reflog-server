package com.github.teamreflog.reflogserver.auth.service;

import com.github.teamreflog.reflogserver.auth.dto.LoginRequest;
import com.github.teamreflog.reflogserver.auth.exception.EmailNotExistException;
import com.github.teamreflog.reflogserver.auth.exception.PasswordNotMatchedException;
import com.github.teamreflog.reflogserver.member.domain.Member;
import com.github.teamreflog.reflogserver.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;

    public void login(final LoginRequest request) {
        final Member member =
                memberRepository
                        .findByEmail(request.getMemberEmail())
                        .orElseThrow(EmailNotExistException::new);

        if (!member.isMatchedPassword(request.password())) {
            throw new PasswordNotMatchedException();
        }
    }
}
