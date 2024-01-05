package com.github.teamreflog.reflogserver.topic.infrastructure;

import com.github.teamreflog.reflogserver.topic.domain.DateProvider;
import java.time.DayOfWeek;
import org.springframework.stereotype.Component;

@Component
public class DateProviderImpl implements DateProvider {

    private DateGenerator dateGenerator;

    @Override
    public DayOfWeek getToday(final String timezone) {
        return dateGenerator.generateDate(timezone);
    }

    public void setDateGenerator(final DateGenerator dateGenerator) {
        this.dateGenerator = dateGenerator;
    }
}
