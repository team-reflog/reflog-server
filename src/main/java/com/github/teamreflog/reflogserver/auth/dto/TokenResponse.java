package com.github.teamreflog.reflogserver.auth.dto;

public record TokenResponse(String accessToken, String refreshToken) {}
