package com.github.teamreflog.reflogserver.auth.config;

import com.github.teamreflog.reflogserver.auth.domain.JwtParser;
import com.github.teamreflog.reflogserver.auth.domain.JwtProvider;
import com.github.teamreflog.reflogserver.auth.domain.MemberPasswordEncoder;
import com.github.teamreflog.reflogserver.auth.infrastructure.JwtParserImpl;
import com.github.teamreflog.reflogserver.auth.infrastructure.JwtProviderImpl;
import com.github.teamreflog.reflogserver.auth.infrastructure.MemberPasswordEncoderImpl;
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
    public JwtProvider jwtProvider(@Value("${jwt.secret}") final String secret) {
        return new JwtProviderImpl(Jwts.builder().signWith(Keys.hmacShaKeyFor(secret.getBytes())));
    }

    @Bean
    public JwtParser jwtParser(@Value("${jwt.secret}") final String secret) {
        return new JwtParserImpl(
                Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build());
    }
}
