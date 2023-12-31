package com.github.teamreflog.reflogserver.team.dto;

import java.time.DayOfWeek;
import java.util.List;

public record TeamCreateRequest(String name, String description, List<DayOfWeek> reflectionDays) {}
