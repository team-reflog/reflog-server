package com.github.teamreflog.reflogserver.topic.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.github.teamreflog.reflogserver.common.config.JpaConfig;
import com.github.teamreflog.reflogserver.topic.application.dto.TopicCreateRequest;
import com.github.teamreflog.reflogserver.topic.domain.exception.NotOwnerException;
import com.github.teamreflog.reflogserver.topic.domain.exception.TeamNotExistException;
import com.github.teamreflog.reflogserver.topic.infrastructure.DateGeneratorImpl;
import com.github.teamreflog.reflogserver.topic.infrastructure.DateProviderImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Import({TopicService.class, JpaConfig.class, DateProviderImpl.class, DateGeneratorImpl.class})
@DisplayName("통합 테스트: TopicService")
class TopicServiceTest {

    @Autowired TopicService topicService;

    @Test
    @Sql({"/member.sql", "/team.sql"})
    @DisplayName("토픽을 생성한다.")
    void createTopic() {
        /* given */
        final Long teamId = 1L;
        final Long ownerId = 1L;
        final TopicCreateRequest request = new TopicCreateRequest(teamId, "오늘 하루는 어땠나요?");

        /* when & then */
        assertThat(topicService.createTopic(ownerId, request)).isEqualTo(1L);
    }

    @Test
    @DisplayName("팀이 존재하지 않는 경우 예외가 발생한다.")
    void throwExceptionWithNotExistTeam() {
        /* given */
        final Long notExistTeamId = 777L;
        final Long memberId = 1L;
        final TopicCreateRequest request = new TopicCreateRequest(notExistTeamId, "오늘 하루는 어땠나요?");

        /* when & then */
        assertThatCode(() -> topicService.createTopic(memberId, request))
                .isInstanceOf(TeamNotExistException.class)
                .hasMessage("존재하지 않는 팀입니다.");
    }

    @Test
    @Sql({"/member.sql", "/team.sql"})
    @DisplayName("팀장이 아닌 경우 토픽을 생성할 때 예외가 발생한다.")
    void throwExceptionWithNotOwner() {
        /* given */
        final Long teamId = 1L;
        final Long memberId = 2L;
        final TopicCreateRequest request = new TopicCreateRequest(teamId, "오늘 하루는 어땠나요?");

        /* when & then */
        assertThatCode(() -> topicService.createTopic(memberId, request))
                .isInstanceOf(NotOwnerException.class)
                .hasMessage("팀장만 주제를 생성할 수 있습니다.");
    }
}
