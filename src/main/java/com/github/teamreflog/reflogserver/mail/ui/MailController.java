package com.github.teamreflog.reflogserver.mail.ui;

import com.github.teamreflog.reflogserver.mail.application.dto.MailSendRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/mails")
public class MailController {

    @PostMapping("/send")
    public ResponseEntity<Void> sendAuthMail(@RequestBody final MailSendRequest request) {

        return ResponseEntity.ok().build();
    }
}
