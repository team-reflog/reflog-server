package com.github.teamreflog.reflogserver.auth.application;

import com.github.teamreflog.reflogserver.auth.application.dto.LoginRequest;
import com.github.teamreflog.reflogserver.auth.application.dto.TokenResponse;
import com.github.teamreflog.reflogserver.auth.exception.PasswordNotMatchedException;
import com.github.teamreflog.reflogserver.auth.infrastructure.JwtProvider;
import com.github.teamreflog.reflogserver.common.exception.ReflogIllegalArgumentException;
import com.github.teamreflog.reflogserver.member.domain.Member;
import com.github.teamreflog.reflogserver.member.domain.MemberEmail;
import com.github.teamreflog.reflogserver.member.domain.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;

    private final JwtProvider jwtProvider;
    private final PasswordEncoder passwordEncoder;

    public TokenResponse login(final LoginRequest request) {
        final Member member =
                memberRepository
                        .findByEmail(new MemberEmail(request.email()))
                        .orElseThrow(ReflogIllegalArgumentException::new);

        if (!passwordEncoder.matches(request.password(), member.getPassword())) {
            throw new PasswordNotMatchedException();
        }

        return new TokenResponse(
                jwtProvider.generateAccessToken(member.getId()),
                jwtProvider.generateRefreshToken(member.getId()));
    }

    public TokenResponse refresh(final String header) {
        final String token = jwtProvider.extractToken(header);
        final Long memberId = jwtProvider.parseSubject(token);

        return new TokenResponse(
                jwtProvider.generateAccessToken(memberId),
                jwtProvider.generateRefreshToken(memberId));
    }
}
