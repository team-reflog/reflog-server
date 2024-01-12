package com.github.teamreflog.reflogserver.auth.config;

import com.github.teamreflog.reflogserver.auth.domain.MemberPasswordEncoder;
import com.github.teamreflog.reflogserver.auth.domain.TokenParser;
import com.github.teamreflog.reflogserver.auth.domain.TokenProvider;
import com.github.teamreflog.reflogserver.auth.infrastructure.MemberPasswordEncoderImpl;
import com.github.teamreflog.reflogserver.auth.infrastructure.TokenParserImpl;
import com.github.teamreflog.reflogserver.auth.infrastructure.TokenProviderImpl;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class AuthConfig {

    @Bean
    public MemberPasswordEncoder passwordEncoder() {
        return new MemberPasswordEncoderImpl(new BCryptPasswordEncoder());
    }

    @Bean
    public TokenProvider jwtProvider(@Value("${jwt.secret}") final String secret) {
        return new TokenProviderImpl(
                Jwts.builder().signWith(Keys.hmacShaKeyFor(secret.getBytes())));
    }

    @Bean
    public TokenParser jwtParser(@Value("${jwt.secret}") final String secret) {
        return new TokenParserImpl(
                Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build());
    }
}
