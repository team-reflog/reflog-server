package com.github.teamreflog.reflogserver.team.application.dto;

import java.time.LocalDate;

public record TeamReflectionQueryResponse(
        Long id, String name, String author, LocalDate reflectionAt) {}
