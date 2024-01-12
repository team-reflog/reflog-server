package com.github.teamreflog.reflogserver.auth.domain;

import com.github.teamreflog.reflogserver.auth.exception.JwtInvalidException;
import org.springframework.stereotype.Component;

@Component
public class JwtExtractor {

    public String extract(final String header) {
        if (header == null || !header.startsWith("Bearer ")) {
            throw new JwtInvalidException();
        }

        return header.substring(7);
    }
}
