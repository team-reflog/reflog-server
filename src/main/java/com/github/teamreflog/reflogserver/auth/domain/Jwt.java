package com.github.teamreflog.reflogserver.auth.domain;

import java.util.Map;

public record Jwt(Map<ClaimType, String> claimByName) {

    public String getClaim(final ClaimType type) {
        return String.valueOf(claimByName.get(type));
    }

    public boolean hasRole(final String name) {
        return claimByName.get(ClaimType.ROLE).equals(name);
    }
}
