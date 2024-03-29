package com.github.teamreflog.reflogserver.acceptance.auth;

import com.github.teamreflog.reflogserver.auth.domain.Authorities;
import com.github.teamreflog.reflogserver.auth.domain.MemberRole;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth-acceptance-test")
public class AuthAcceptanceTestController {

    @GetMapping("/no-auth")
    public void noAuth() {}

    @Authorities(MemberRole.MEMBER)
    @GetMapping("/member-auth")
    public void memberAuth() {}

    @Authorities(MemberRole.MEMBER)
    @RequestMapping(value = "/preflight", method = RequestMethod.OPTIONS)
    public void options() {}
}
