package com.github.teamreflog.reflogserver.auth.infrastructure;

import com.github.teamreflog.reflogserver.auth.domain.ClaimType;
import com.github.teamreflog.reflogserver.auth.domain.Jwt;
import com.github.teamreflog.reflogserver.auth.domain.JwtParser;
import com.github.teamreflog.reflogserver.auth.exception.JwtInvalidException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import java.util.EnumMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

@RequiredArgsConstructor
public class JwtParserImpl implements JwtParser {

    private final io.jsonwebtoken.JwtParser parser;

    @Override
    public Jwt parse(final String token) {
        if (!StringUtils.hasText(token)) {
            throw new JwtInvalidException();
        }

        try {
            final Claims claims = (Claims) parser.parse(token).getPayload();

            final Map<ClaimType, String> claimByName = new EnumMap<>(ClaimType.class);
            for (final ClaimType type : ClaimType.values()) {
                final String claimName = type.getClaimName();
                if (!claims.containsKey(claimName)) {
                    throw new JwtInvalidException();
                }

                claimByName.put(type, claims.get(claimName, String.class));
            }

            return new Jwt(claimByName);
        } catch (final JwtException e) {
            throw new JwtInvalidException(e);
        }
    }
}
