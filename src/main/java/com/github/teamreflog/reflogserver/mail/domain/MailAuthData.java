package com.github.teamreflog.reflogserver.mail.domain;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MailAuthData {

    @Getter private final String email;
    @Getter private final Integer code;
    private final Boolean isVerified;

    public static MailAuthData of(@NonNull final String mail, @NonNull final Integer number) {
        return new MailAuthData(mail, number, false);
    }
}
