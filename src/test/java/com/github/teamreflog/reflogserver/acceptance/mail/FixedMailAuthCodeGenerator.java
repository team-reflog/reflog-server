package com.github.teamreflog.reflogserver.acceptance.mail;

import com.github.teamreflog.reflogserver.mail.domain.MailAuthCodeGenerator;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class FixedMailAuthCodeGenerator implements MailAuthCodeGenerator {

    @Override
    public Integer generateCode() {
        return 240114;
    }
}
