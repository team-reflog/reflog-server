package com.github.teamreflog.reflogserver.topic.domain;

import java.time.DayOfWeek;

public interface DayOfWeekProvider {

    DayOfWeek getToday(String timezone);
}
