package com.github.teamreflog.reflogserver.mail.ui;

import com.github.teamreflog.reflogserver.mail.application.MailService;
import com.github.teamreflog.reflogserver.mail.application.dto.MailSendRequest;
import com.github.teamreflog.reflogserver.mail.application.dto.MailSendResponse;
import com.github.teamreflog.reflogserver.mail.application.dto.MailVerifyRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mails")
@RequiredArgsConstructor
public class MailController {

    private final MailService mailService;

    @PostMapping("/send")
    public MailSendResponse sendAuthMail(@RequestBody final MailSendRequest request) {
        return mailService.sendAuthMail(request);
    }

    @PostMapping("/verify")
    public void verifyAuthMail(@RequestBody final MailVerifyRequest request) {
        mailService.verifyAuthMail(request);
    }
}
