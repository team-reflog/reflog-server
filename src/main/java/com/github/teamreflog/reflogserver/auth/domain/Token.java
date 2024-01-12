package com.github.teamreflog.reflogserver.auth.domain;

import java.util.Map;

public record Token(Map<ClaimType, String> claimByName) {

    public String getClaim(final ClaimType type) {
        return claimByName.get(type);
    }

    public boolean hasRole(final String name) {
        return claimByName.get(ClaimType.ROLE).equals(name);
    }
}
