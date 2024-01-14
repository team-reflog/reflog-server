package com.github.teamreflog.reflogserver.mail.domain;

public interface MailSender {

    void sendAuthMail(final String mail, final Integer code);
}
