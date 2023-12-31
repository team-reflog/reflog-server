package com.github.teamreflog.reflogserver.member.domain;

import com.github.teamreflog.reflogserver.member.exception.EmailFormatException;
import jakarta.persistence.Embeddable;

@Embeddable
public record MemberEmail(String email) {

    public MemberEmail {
        if (!email.matches("^[a-zA-Z0-9._]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$")) {
            throw new EmailFormatException();
        }
    }
}
