package com.github.teamreflog.reflogserver.mail.domain;

public interface MailRepository {

    String save(final MailAuthData mailAuthData);

    MailAuthData getById(final String authMailId);

    void update(String id, MailAuthData newData);
}
