package com.github.teamreflog.reflogserver.topic.infrastructure;

import com.github.teamreflog.reflogserver.topic.domain.DayOfWeekProvider;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import org.springframework.stereotype.Component;

// TODO: class로 옮기기
@Component
public class DayOfWeekProviderImpl implements DayOfWeekProvider {

    private static final DayOfWeekGenerator DEFAULT_DAY_OF_WEEK_GENERATOR =
            timezone -> ZonedDateTime.of(LocalDateTime.now(), ZoneId.of(timezone)).getDayOfWeek();
    private DayOfWeekGenerator dayOfWeekGenerator = DEFAULT_DAY_OF_WEEK_GENERATOR;

    @Override
    public DayOfWeek getToday(final String timezone) {
        return dayOfWeekGenerator.generateDayOfWeek(timezone);
    }

    public void setDayOfWeekGenerator(final DayOfWeekGenerator dayOfWeekGenerator) {
        this.dayOfWeekGenerator = dayOfWeekGenerator;
    }

    public void setDefaultDayOfWeekGenerator() {
        this.dayOfWeekGenerator = DEFAULT_DAY_OF_WEEK_GENERATOR;
    }

    public interface DayOfWeekGenerator {

        DayOfWeek generateDayOfWeek(String timezone);
    }
}
