package com.github.teamreflog.reflogserver.topic.domain;

import java.util.List;
import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface TopicRepository extends Repository<Topic, Long> {

    List<Topic> findAllByTeamId(Long teamId);

    Topic save(Topic topic);

    Optional<Topic> findById(Long id);
}
