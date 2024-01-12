package com.github.teamreflog.reflogserver.reflection.domain;

import java.util.List;
import org.springframework.data.repository.Repository;

public interface CommentRepository extends Repository<Comment, Long> {

    Comment save(Comment comment);

    List<Comment> findAllByReflectionId(Long reflectionId);
}
