package com.github.teamreflog.reflogserver.auth.infrastructure;

import com.github.teamreflog.reflogserver.auth.domain.ClaimType;
import com.github.teamreflog.reflogserver.auth.domain.MemberRole;
import com.github.teamreflog.reflogserver.auth.domain.TokenProvider;
import io.jsonwebtoken.JwtBuilder;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class TokenProviderImpl implements TokenProvider {

    private static final long ACCESS_TOKEN_EXPIRATION_MILLIS = TimeUnit.HOURS.toMillis(1);
    private static final long REFRESH_TOKEN_EXPIRATION_MILLIS = TimeUnit.DAYS.toMillis(7);

    private final JwtBuilder builder;

    @Override
    public String generateAccessToken(final Long id) {
        return generateToken(id, ACCESS_TOKEN_EXPIRATION_MILLIS);
    }

    @Override
    public String generateRefreshToken(final Long id) {
        return generateToken(id, REFRESH_TOKEN_EXPIRATION_MILLIS);
    }

    private String generateToken(final Long id, final long duration) {
        return builder.claim(ClaimType.MEMBER_ID.getClaimName(), String.valueOf(id))
                .claim(ClaimType.ROLE.getClaimName(), MemberRole.MEMBER)
                .expiration(new Date(System.currentTimeMillis() + duration))
                .compact();
    }
}
