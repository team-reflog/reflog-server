package com.github.teamreflog.reflogserver.auth.domain;

import java.util.Map;

public record Jwt(Map<String, Object> claims) {

    public String getClaim(final ClaimType type) {
        return String.valueOf(claims.get(type.getClaimName()));
    }
}
