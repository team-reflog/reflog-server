package com.github.teamreflog.reflogserver.auth.domain;

public interface JwtProvider {

    String generateAccessToken(final Long memberId);

    String generateRefreshToken(final Long memberId);
}
