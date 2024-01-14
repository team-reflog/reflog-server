package com.github.teamreflog.reflogserver.acceptance.topic;

import com.github.teamreflog.reflogserver.topic.application.TopicService;
import com.github.teamreflog.reflogserver.topic.domain.DayOfWeekProvider;
import java.lang.reflect.Field;
import org.springframework.aop.framework.Advised;

public abstract class DayOfWeekProviderBeanChanger {

    private DayOfWeekProviderBeanChanger() {
        /* no-op */
    }

    public static void changeDateProvider(
            final TopicService topicService, final DayOfWeekProvider dayOfWeekProvider) {
        try {
            final Advised advised = (Advised) topicService;
            final TopicService service = (TopicService) advised.getTargetSource().getTarget();
            final Class<TopicService> clazz = TopicService.class;
            final Field field = clazz.getDeclaredField("dayOfWeekProvider");
            field.setAccessible(true);

            field.set(service, dayOfWeekProvider);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}
