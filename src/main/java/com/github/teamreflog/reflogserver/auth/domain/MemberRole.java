package com.github.teamreflog.reflogserver.auth.domain;

public enum MemberRole {
    MEMBER,
    ;

    public boolean matches(final String role) {
        return name().equals(role);
    }
}
