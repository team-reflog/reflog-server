package com.github.teamreflog.reflogserver.mail.infrastructure;

import com.github.teamreflog.reflogserver.mail.domain.MailSender;
import org.springframework.stereotype.Component;

@Component
public class GmailSender implements MailSender {

    @Override
    public void sendAuthMail(final String mail, final Integer code) {
        // TODO: Gmail SMTP 서버를 이용해 메일을 보내는 로직을 구현
    }
}
