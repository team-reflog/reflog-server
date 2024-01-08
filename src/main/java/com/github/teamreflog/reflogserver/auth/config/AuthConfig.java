package com.github.teamreflog.reflogserver.auth.config;

import com.github.teamreflog.reflogserver.auth.domain.MemberPasswordEncoder;
import com.github.teamreflog.reflogserver.auth.infrastructure.MemberPasswordEncoderImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class AuthConfig {

    @Bean
    public MemberPasswordEncoder passwordEncoder() {
        return new MemberPasswordEncoderImpl(new BCryptPasswordEncoder());
    }
}
