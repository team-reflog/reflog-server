package com.github.teamreflog.reflogserver.topic.domain;

import com.github.teamreflog.reflogserver.topic.domain.exception.ExceedMaxSizeException;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Topics {

    private static final int MAX_SIZE = 5;

    private final List<Topic> topics;

    public static Topics from(final List<Topic> topics) {
        if (topics.size() > MAX_SIZE) {
            throw new ExceedMaxSizeException();
        }

        return new Topics(topics);
    }

    public void addTopic(final Topic topic) {
        if (isFull()) {
            throw new ExceedMaxSizeException();
        }

        topics.add(topic);
    }

    private boolean isFull() {
        return topics.size() == MAX_SIZE;
    }
}
