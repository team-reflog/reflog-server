package com.github.teamreflog.reflogserver.auth.domain;

public interface TokenParser {

    Token parse(final String token);
}
