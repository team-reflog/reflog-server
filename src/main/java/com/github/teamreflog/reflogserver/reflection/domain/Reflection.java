package com.github.teamreflog.reflogserver.reflection.domain;

import com.github.teamreflog.reflogserver.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "reflections")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Reflection extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false, updatable = false)
    private Long memberId;

    @Column(name = "topic_id", nullable = false, updatable = false)
    private Long topicId;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false, updatable = false)
    private String content;

    public static Reflection of(final Long memberId, final Long topicId, final String content) {
        return new Reflection(null, memberId, topicId, content);
    }
}
