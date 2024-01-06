package com.github.teamreflog.reflogserver.acceptance.helper;

import com.github.teamreflog.reflogserver.topic.application.TopicService;
import com.github.teamreflog.reflogserver.topic.domain.DayOfWeekProvider;
import java.lang.reflect.Field;

public abstract class DayOfWeekProviderBeanChanger {

    private DayOfWeekProviderBeanChanger() {
        /* no-op */
    }

    public static void changeDateProvider(
            final TopicService topicService, final DayOfWeekProvider dayOfWeekProvider) {
        try {
            final Class clazz = TopicService.class;
            final Field field = clazz.getDeclaredField("dayOfWeekProvider");
            field.setAccessible(true);

            field.set(topicService, dayOfWeekProvider);

        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}
