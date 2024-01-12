package com.github.teamreflog.reflogserver.auth.application;

import com.github.teamreflog.reflogserver.auth.application.dto.LoginRequest;
import com.github.teamreflog.reflogserver.auth.application.dto.TokenResponse;
import com.github.teamreflog.reflogserver.auth.domain.JwtExtractor;
import com.github.teamreflog.reflogserver.auth.domain.Token;
import com.github.teamreflog.reflogserver.auth.domain.TokenParser;
import com.github.teamreflog.reflogserver.auth.domain.TokenProvider;
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
    private final TokenProvider tokenProvider;
    private final TokenParser tokenParser;
    private final JwtExtractor jwtExtractor;

    public TokenResponse login(final LoginRequest request) {
        final Member member =
                memberRepository
                        .findByEmail(new MemberEmail(request.email()))
                        .orElseThrow(ReflogIllegalArgumentException::new);

        memberValidator.validatePassword(member, request.password());

        return new TokenResponse(
                tokenProvider.generateAccessToken(member.getId()),
                tokenProvider.generateRefreshToken(member.getId()));
    }

    public TokenResponse refresh(final String header) {
        final String token = jwtExtractor.extract(header);
        final Token jwt = tokenParser.parse(token);
        final Long memberId = jwt.getSubject();

        return new TokenResponse(
                tokenProvider.generateAccessToken(memberId),
                tokenProvider.generateRefreshToken(memberId));
    }
}
