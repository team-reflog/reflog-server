package com.github.teamreflog.reflogserver.auth.service;

import com.github.teamreflog.reflogserver.auth.dto.LoginRequest;
import com.github.teamreflog.reflogserver.auth.dto.TokenResponse;
import com.github.teamreflog.reflogserver.auth.exception.EmailNotExistException;
import com.github.teamreflog.reflogserver.auth.exception.PasswordNotMatchedException;
import com.github.teamreflog.reflogserver.auth.infrastructure.JwtProvider;
import com.github.teamreflog.reflogserver.member.domain.Member;
import com.github.teamreflog.reflogserver.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final JwtProvider jwtProvider;

    public TokenResponse login(final LoginRequest request) {
        final Member member =
                memberRepository
                        .findByEmail(request.getMemberEmail())
                        .orElseThrow(EmailNotExistException::new);

        if (!member.isMatchedPassword(request.password())) {
            throw new PasswordNotMatchedException();
        }

        return new TokenResponse(
                jwtProvider.generateAccessToken(member.getId()),
                jwtProvider.generateRefreshToken(member.getId()));
    }

    public TokenResponse refresh(final String token) {
        final Long memberId = jwtProvider.parseSubject(token);

        return new TokenResponse(
                jwtProvider.generateAccessToken(memberId),
                jwtProvider.generateRefreshToken(memberId));
    }
}
