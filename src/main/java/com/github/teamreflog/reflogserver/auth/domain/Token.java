package com.github.teamreflog.reflogserver.auth.domain;

import com.github.teamreflog.reflogserver.auth.exception.JwtInvalidException;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Token {

    private final Map<ClaimType, String> claimByName;

    public static Token from(final Map<ClaimType, String> claimByName) {
        if (!claimByName.keySet().containsAll(List.of(ClaimType.values()))) {
            throw new JwtInvalidException();
        }

        return new Token(claimByName);
    }

    public Long getSubject() {
        return Long.valueOf(claimByName.get(ClaimType.MEMBER_ID));
    }

    public boolean hasRole(final MemberRole role) {
        return role.matches(claimByName.get(ClaimType.ROLE));
    }
}
