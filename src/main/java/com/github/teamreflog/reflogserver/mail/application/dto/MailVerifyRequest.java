package com.github.teamreflog.reflogserver.mail.application.dto;

public record MailVerifyRequest(String authMailId, Integer authMailNumber) {}
