package com.github.teamreflog.reflogserver.auth.controller;

import com.github.teamreflog.reflogserver.auth.dto.LoginResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @PostMapping("/login")
    public LoginResponse login() {
        return new LoginResponse("token");
    }
}
