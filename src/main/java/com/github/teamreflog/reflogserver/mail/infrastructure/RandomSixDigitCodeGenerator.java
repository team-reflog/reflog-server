package com.github.teamreflog.reflogserver.mail.infrastructure;

import com.github.teamreflog.reflogserver.mail.domain.MailAuthCodeGenerator;
import java.util.concurrent.ThreadLocalRandom;
import org.springframework.stereotype.Component;

@Component
public class RandomSixDigitCodeGenerator implements MailAuthCodeGenerator {

    @Override
    public Integer generateCode() {
        return ThreadLocalRandom.current().nextInt(100000, 1000000);
    }
}
