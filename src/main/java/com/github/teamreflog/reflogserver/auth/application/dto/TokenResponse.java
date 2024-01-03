package com.github.teamreflog.reflogserver.auth.application.dto;

public record TokenResponse(String accessToken, String refreshToken) {}
