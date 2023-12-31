package com.github.teamreflog.reflogserver.auth.dto;

public record LoginResponse(String accessToken, String refreshToken) {}
