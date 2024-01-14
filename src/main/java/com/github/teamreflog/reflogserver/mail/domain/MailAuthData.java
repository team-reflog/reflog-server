package com.github.teamreflog.reflogserver.mail.domain;

import com.github.teamreflog.reflogserver.mail.exception.MailAuthCodeNotMatchedException;
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

    public MailAuthData verify(final Integer code) {
        if (!isMatchedCode(code)) {
            throw new MailAuthCodeNotMatchedException();
        }

        return new MailAuthData(email, code, true);
    }

    private boolean isMatchedCode(final Integer code) {
        return this.code.equals(code);
    }
}
