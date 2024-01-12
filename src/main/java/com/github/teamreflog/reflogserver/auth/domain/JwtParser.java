package com.github.teamreflog.reflogserver.auth.domain;

public interface JwtParser {

    Jwt parse(final String token);
}
