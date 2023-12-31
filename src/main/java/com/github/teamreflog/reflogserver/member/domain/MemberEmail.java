package com.github.teamreflog.reflogserver.member.domain;

import com.github.teamreflog.reflogserver.member.exception.EmailFormatException;
import jakarta.persistence.Embeddable;
import java.util.regex.Pattern;

@Embeddable
public record MemberEmail(String email) {

    private static final Pattern emailPattern =
            Pattern.compile("^[a-zA-Z0-9._]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,6}$");

    public MemberEmail {
        if (emailPattern.matcher(email).matches()) {
            throw new EmailFormatException();
        }
    }
}
