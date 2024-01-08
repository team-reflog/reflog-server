package com.github.teamreflog.reflogserver.auth.application;

import com.github.teamreflog.reflogserver.auth.application.dto.LoginRequest;
import com.github.teamreflog.reflogserver.auth.application.dto.TokenResponse;
import com.github.teamreflog.reflogserver.auth.domain.JwtProvider;
import com.github.teamreflog.reflogserver.common.exception.ReflogIllegalArgumentException;
import com.github.teamreflog.reflogserver.member.domain.Member;
import com.github.teamreflog.reflogserver.member.domain.MemberEmail;
import com.github.teamreflog.reflogserver.member.domain.MemberRepository;
import com.github.teamreflog.reflogserver.member.domain.MemberValidator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final MemberValidator memberValidator; // TODO: auth -> member 의존 중 -> 같은 도메인 영역이라는 힌트!
    private final JwtProvider jwtProvider;

    public TokenResponse login(final LoginRequest request) {
        final Member member =
                memberRepository
                        .findByEmail(new MemberEmail(request.email()))
                        .orElseThrow(ReflogIllegalArgumentException::new);

        memberValidator.validatePassword(member, request.password());

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
