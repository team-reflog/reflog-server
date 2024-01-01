package com.github.teamreflog.reflogserver.topic.repository;

import com.github.teamreflog.reflogserver.topic.domain.Topic;
import java.util.List;
import org.springframework.data.repository.Repository;

public interface TopicRepository extends Repository<Topic, Long> {

    List<Topic> findAllByTeamId(Long teamId);

    Topic save(Topic topic);
}
