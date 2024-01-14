package com.github.teamreflog.reflogserver.mail.domain;

public interface MailRepository {

    String save(MailAuthData mailAuthData);

    MailAuthData getById(String id);

    void update(String id, MailAuthData newData);
}
