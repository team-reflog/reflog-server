package com.github.teamreflog.reflogserver.auth.domain;

import java.util.Map;

public record Token(Map<ClaimType, String> claimByName) {

    public Long getSubject() {
        return Long.valueOf(claimByName.get(ClaimType.MEMBER_ID));
    }

    public boolean hasRole(final String name) {
        return claimByName.get(ClaimType.ROLE).equals(name);
    }
}
