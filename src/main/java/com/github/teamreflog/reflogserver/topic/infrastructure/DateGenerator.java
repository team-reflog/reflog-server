package com.github.teamreflog.reflogserver.topic.infrastructure;

import java.time.DayOfWeek;

public interface DateGenerator {

    DayOfWeek generateDate(String timezone);
}
