package com.github.teamreflog.reflogserver.mail.infrastructure;

import com.github.teamreflog.reflogserver.common.exception.ReflogIllegalArgumentException;
import com.github.teamreflog.reflogserver.mail.domain.MailAuthData;
import com.github.teamreflog.reflogserver.mail.domain.MailRepository;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.stereotype.Component;

@Component
public class MailRepositoryImpl implements MailRepository {

    // TODO: 캐시로 변경
    private final Map<String, MailAuthData> mailAuthDataById = new ConcurrentHashMap<>();

    @Override
    public String save(final MailAuthData mailAuthData) {
        final String id = UUID.randomUUID().toString();
        mailAuthDataById.put(id, mailAuthData);

        return id;
    }

    @Override
    public MailAuthData getById(final String id) {
        if (!mailAuthDataById.containsKey(id)) {
            throw new ReflogIllegalArgumentException();
        }

        return mailAuthDataById.get(id);
    }
}
