package com.github.teamreflog.reflogserver.auth.domain;

public interface MemberPasswordEncoder {

    boolean matches(String rawPassword, String encodedPassword);

    String encode(String password);
}
