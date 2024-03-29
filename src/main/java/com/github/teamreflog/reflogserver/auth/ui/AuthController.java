package com.github.teamreflog.reflogserver.auth.ui;

import com.github.teamreflog.reflogserver.auth.application.AuthService;
import com.github.teamreflog.reflogserver.auth.application.dto.LoginRequest;
import com.github.teamreflog.reflogserver.auth.application.dto.TokenResponse;
import com.github.teamreflog.reflogserver.auth.domain.Authorities;
import com.github.teamreflog.reflogserver.auth.domain.MemberRole;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    public TokenResponse login(@RequestBody final LoginRequest request) {
        return authService.login(request);
    }

    @Authorities(MemberRole.MEMBER)
    @PostMapping("/refresh")
    public TokenResponse refresh(
            @RequestHeader(HttpHeaders.AUTHORIZATION) final String refreshToken) {
        return authService.refresh(refreshToken);
    }
}
