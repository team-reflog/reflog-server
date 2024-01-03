package com.github.teamreflog.reflogserver.topic.domain;

import static org.assertj.core.api.Assertions.assertThatCode;

import com.github.teamreflog.reflogserver.topic.domain.exception.ExceedMaxSizeException;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("단위 테스트: Topics")
class TopicsTest {

    @Test
    @DisplayName("주제의 최대 개수가 넘으면 예외가 발생한다.")
    void throwExceptionExceedMaxSize() {
        /* given */
        final List<Topic> exceedTopicList =
                List.of(
                        Topic.of(1L, "오늘은 무엇을 먹었나요?"),
                        Topic.of(1L, "오늘 기분은 어떠신가요?"),
                        Topic.of(1L, "오늘 하루는 어땠나요?"),
                        Topic.of(1L, "어제랑 달라진 점은 있나요?"),
                        Topic.of(1L, "내일 계획은 뭔가요?"),
                        Topic.of(1L, "가장 기억에 남는 일은 무엇인가요?"));

        /* when & then */
        assertThatCode(() -> Topics.from(exceedTopicList))
                .isInstanceOf(ExceedMaxSizeException.class)
                .hasMessage("주제의 최대 개수를 초과하였습니다.");
    }

    @Test
    @DisplayName("주제의 최대 개수일 때 추가하면 예외가 발생한다.")
    void throwExceptionIsFull() {
        /* given */
        final Topics topics =
                Topics.from(
                        List.of(
                                Topic.of(1L, "오늘은 무엇을 먹었나요?"),
                                Topic.of(1L, "오늘 기분은 어떠신가요?"),
                                Topic.of(1L, "오늘 하루는 어땠나요?"),
                                Topic.of(1L, "어제랑 달라진 점은 있나요?"),
                                Topic.of(1L, "내일 계획은 뭔가요?")));
        final Topic exceedTopic = Topic.of(1L, "가장 기억에 남는 일은 무엇인가요?");

        /* when & then */
        assertThatCode(() -> topics.addTopic(exceedTopic))
                .isInstanceOf(ExceedMaxSizeException.class)
                .hasMessage("주제의 최대 개수를 초과하였습니다.");
    }
}
