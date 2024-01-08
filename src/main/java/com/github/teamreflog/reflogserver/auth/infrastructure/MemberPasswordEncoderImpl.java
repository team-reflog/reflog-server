package com.github.teamreflog.reflogserver.auth.infrastructure;

import com.github.teamreflog.reflogserver.auth.domain.MemberPasswordEncoder;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@RequiredArgsConstructor
public class MemberPasswordEncoderImpl implements MemberPasswordEncoder {

    private final PasswordEncoder passwordEncoder;

    @Override
    public boolean matches(final String rawPassword, final String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    @Override
    public String encode(final String password) {
        return passwordEncoder.encode(password);
    }
}
