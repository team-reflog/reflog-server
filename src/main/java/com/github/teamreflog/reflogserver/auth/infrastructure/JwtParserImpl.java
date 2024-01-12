package com.github.teamreflog.reflogserver.auth.infrastructure;

import com.github.teamreflog.reflogserver.auth.domain.ClaimType;
import com.github.teamreflog.reflogserver.auth.domain.Jwt;
import com.github.teamreflog.reflogserver.auth.domain.JwtParser;
import com.github.teamreflog.reflogserver.auth.exception.JwtInvalidException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class JwtParserImpl implements JwtParser {

    private final io.jsonwebtoken.JwtParser parser;

    @Override
    public Jwt parse(final String token) {
        try {
            final Claims claims = (Claims) parser.parse(token).getPayload();

            return new Jwt(
                    Arrays.stream(ClaimType.values())
                            .collect(
                                    Collectors.toUnmodifiableMap(
                                            ClaimType::getClaimName,
                                            claimType -> claims.get(claimType.getClaimName()))));
        } catch (final JwtException e) {
            throw new JwtInvalidException(e);
        }
    }
}
