package com.github.teamreflog.reflogserver.mail.application;

import com.github.teamreflog.reflogserver.mail.application.dto.MailSendRequest;
import com.github.teamreflog.reflogserver.mail.application.dto.MailSendResponse;
import com.github.teamreflog.reflogserver.mail.application.dto.MailVerifyRequest;
import com.github.teamreflog.reflogserver.mail.domain.MailAuthCodeGenerator;
import com.github.teamreflog.reflogserver.mail.domain.MailAuthData;
import com.github.teamreflog.reflogserver.mail.domain.MailRepository;
import com.github.teamreflog.reflogserver.mail.domain.MailSender;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class MailService {

    private final MailAuthCodeGenerator mailAuthCodeGenerator;
    private final MailSender mailSender;
    private final MailRepository mailRepository;

    public MailSendResponse sendAuthMail(final MailSendRequest request) {
        final MailAuthData data =
                MailAuthData.of(request.email(), mailAuthCodeGenerator.generateCode());

        mailSender.sendAuthMail(data.getEmail(), data.getCode());

        return new MailSendResponse(mailRepository.save(data));
    }

    public void verifyAuthMail(final MailVerifyRequest request) {
        final MailAuthData authData = mailRepository.getById(request.id());

        mailRepository.update(request.id(), authData.verify(request.code()));
    }
}
