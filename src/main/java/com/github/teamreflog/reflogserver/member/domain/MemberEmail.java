package com.github.teamreflog.reflogserver.member.domain;

import com.github.teamreflog.reflogserver.member.domain.exception.EmailFormatException;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;

@Embeddable
public record MemberEmail(@Column(name = "email", unique = true, nullable = false) String email) {

    private static final Pattern emailPattern =
            Pattern.compile("^[a-zA-Z0-9._]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");

    public MemberEmail {
        if (!emailPattern.matcher(email).matches()) {
            throw new EmailFormatException();
        }
    }
}
