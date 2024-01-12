package com.github.teamreflog.reflogserver.auth.infrastructure;

import com.github.teamreflog.reflogserver.auth.domain.ClaimType;
import com.github.teamreflog.reflogserver.auth.domain.Token;
import com.github.teamreflog.reflogserver.auth.domain.TokenParser;
import com.github.teamreflog.reflogserver.auth.exception.JwtInvalidException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import java.util.Arrays;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;

// TODO: JWT exception 분기 상세화 후 추상화
@RequiredArgsConstructor
public class TokenParserImpl implements TokenParser {

    private final io.jsonwebtoken.JwtParser parser;

    @Override
    public Token parse(final String header) {
        final String token = extract(header);

        try {
            final Claims claims = (Claims) parser.parse(token).getPayload();

            return Token.from(
                    Arrays.stream(ClaimType.values())
                            .filter(type -> claims.containsKey(type.getClaimName()))
                            .collect(
                                    Collectors.toMap(
                                            type -> type,
                                            type ->
                                                    claims.get(
                                                            type.getClaimName(), String.class))));
        } catch (final JwtException e) {
            throw new JwtInvalidException(e);
        }
    }

    String extract(final String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            throw new JwtInvalidException();
        }

        return header.substring(7);
    }
}
