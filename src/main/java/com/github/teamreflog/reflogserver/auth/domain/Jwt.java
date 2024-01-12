package com.github.teamreflog.reflogserver.auth.domain;

import java.util.Map;

public record Jwt(Map<ClaimType, Object> claimByName) {

    public String getClaim(final ClaimType type) {
        return String.valueOf(claimByName.get(type));
    }
}
