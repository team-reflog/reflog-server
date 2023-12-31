package com.github.teamreflog.reflogserver.auth.infrastructure;

import com.github.teamreflog.reflogserver.auth.exception.JwtInvalidException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import javax.crypto.SecretKey;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class JwtProvider {

    private static final long ACCESS_TOKEN_EXPIRATION_MILLIS = TimeUnit.HOURS.toMillis(1);
    private static final long REFRESH_TOKEN_EXPIRATION_MILLIS = TimeUnit.DAYS.toMillis(7);

    private final JwtParser parser;
    private final JwtBuilder builder;

    public JwtProvider(@Value("${jwt.secret}") final String secret) {
        final SecretKey key = Keys.hmacShaKeyFor(secret.getBytes());

        this.builder = Jwts.builder().signWith(key);
        this.parser = Jwts.parser().verifyWith(key).build();
    }

    public String generateAccessToken(final Long id) {
        return generateToken(id, ACCESS_TOKEN_EXPIRATION_MILLIS);
    }

    public String generateRefreshToken(final Long id) {
        return generateToken(id, REFRESH_TOKEN_EXPIRATION_MILLIS);
    }

    private String generateToken(final Long id, final long duration) {
        return builder.subject(String.valueOf(id))
                .expiration(new Date(System.currentTimeMillis() + duration))
                .compact();
    }

    public Long parseSubject(final String token) {
        try {
            final Claims payload = (Claims) parser.parse(token).getPayload();

            return Long.valueOf(payload.getSubject());
        } catch (final JwtException e) {
            throw new JwtInvalidException(e);
        }
    }

    public String extractToken(final String header) {
        if (!StringUtils.hasText(header) || !header.startsWith("Bearer ")) {
            throw new JwtInvalidException();
        }

        return header.replace("Bearer ", "");
    }
}
