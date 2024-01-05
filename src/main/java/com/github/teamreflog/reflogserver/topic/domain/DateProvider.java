package com.github.teamreflog.reflogserver.topic.domain;

import java.time.DayOfWeek;

public interface DateProvider {

    DayOfWeek getToday(String timezone);
}
