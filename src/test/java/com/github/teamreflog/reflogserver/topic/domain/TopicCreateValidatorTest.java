package com.github.teamreflog.reflogserver.topic.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.mock;

import com.github.teamreflog.reflogserver.topic.domain.exception.NotOwnerException;
import java.time.DayOfWeek;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("단위 테스트: TopicCreateValidator")
class TopicCreateValidatorTest {

    TopicCreateValidator topicCreateValidator;

    @BeforeEach
    void setUp() {
        topicCreateValidator = new TopicCreateValidator(mock(TeamQueryClient.class));
    }

    @Test
    @DisplayName("팀장이 아닌 경우 토픽을 생성할 때 예외가 발생한다.")
    void throwExceptionWithNotOwner() {
        /* given */
        final Long notOwnerId = 2L;
        final TeamData data =
                new TeamData(
                        1L,
                        1L,
                        List.of(
                                DayOfWeek.MONDAY,
                                DayOfWeek.TUESDAY,
                                DayOfWeek.WEDNESDAY,
                                DayOfWeek.THURSDAY));

        /* when & then */
        assertThatCode(() -> topicCreateValidator.validateTeamOwnerAuthorization(data, notOwnerId))
                .isInstanceOf(NotOwnerException.class)
                .hasMessage("팀장만 주제를 생성할 수 있습니다.");
    }
}
