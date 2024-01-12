package com.github.teamreflog.reflogserver.auth.domain;

import java.util.Map;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class Token {

    private final Map<ClaimType, String> claimByName;

    public Long getSubject() {
        return Long.valueOf(claimByName.get(ClaimType.MEMBER_ID));
    }

    public boolean hasRole(final String name) {
        return claimByName.get(ClaimType.ROLE).equals(name);
    }
}
