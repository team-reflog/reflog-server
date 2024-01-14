package com.github.teamreflog.reflogserver.mail.ui;

import com.github.teamreflog.reflogserver.mail.application.dto.MailSendRequest;
import com.github.teamreflog.reflogserver.mail.application.dto.MailSendResponse;
import com.github.teamreflog.reflogserver.mail.application.dto.MailVerifyRequest;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mails")
public class MailController {

    @PostMapping("/send")
    public MailSendResponse sendAuthMail(@RequestBody final MailSendRequest request) {

        return new MailSendResponse(UUID.randomUUID().toString());
    }

    @PostMapping("/verify")
    public ResponseEntity<Void> verifyAuthMail(@RequestBody final MailVerifyRequest request) {

        return ResponseEntity.ok().build();
    }
}
