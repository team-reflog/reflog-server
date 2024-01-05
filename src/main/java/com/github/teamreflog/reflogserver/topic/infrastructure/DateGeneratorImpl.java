package com.github.teamreflog.reflogserver.topic.infrastructure;

import java.time.DayOfWeek;
import org.springframework.stereotype.Component;

@Component
public class DateGeneratorImpl implements DateGenerator {

    // TODO: implement
    @Override
    public DayOfWeek generateDate(final String timezone) {
        /*
         *
         * 사용자가 주제를 조회한다.
         * 서버는 사용자 국가에 맞는 요일에 해당하는 주제를 찾아 반환한다.
         *
         * */
        return null;
    }
}
