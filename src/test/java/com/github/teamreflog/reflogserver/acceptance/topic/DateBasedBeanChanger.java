package com.github.teamreflog.reflogserver.acceptance.topic;

import com.github.teamreflog.reflogserver.reflection.application.ReflectionService;
import com.github.teamreflog.reflogserver.reflection.domain.DateProvider;
import com.github.teamreflog.reflogserver.topic.application.TopicService;
import com.github.teamreflog.reflogserver.topic.domain.DayOfWeekProvider;
import java.lang.reflect.Field;
import org.springframework.aop.framework.Advised;

public abstract class DateBasedBeanChanger {

    private DateBasedBeanChanger() {
        /* no-op */
    }

    public static void changeDayOfWeekProvider(
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

    public static void changeDateProvider(
            final ReflectionService reflectionService, final DateProvider dateProvider) {
        try {
            final Advised advised = (Advised) reflectionService;
            final ReflectionService service =
                    (ReflectionService) advised.getTargetSource().getTarget();
            final Class<ReflectionService> clazz = ReflectionService.class;
            final Field field = clazz.getDeclaredField("dateProvider");
            field.setAccessible(true);

            field.set(service, dateProvider);
        } catch (final Exception e) {
            throw new RuntimeException(e);
        }
    }
}
