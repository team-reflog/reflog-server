package com.github.teamreflog.reflogserver.mail.infrastructure;

import com.github.teamreflog.reflogserver.mail.domain.MailAuthCodeGenerator;
import java.security.SecureRandom;
import org.springframework.stereotype.Component;

@Component
public class RandomSixDigitCodeGenerator implements MailAuthCodeGenerator {

    private final SecureRandom secureRandom = new SecureRandom();

    @Override
    public Integer generateCode() {
        return secureRandom.nextInt(100000, 1000000);
    }
}
