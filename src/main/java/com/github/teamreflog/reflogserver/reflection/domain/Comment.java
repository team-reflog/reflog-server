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
@Table(name = "comments")
@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "crew_id", nullable = false)
    private Long crewId;

    @Column(name = "reflection_id", nullable = false)
    private Long reflectionId;

    @Column(name = "content", nullable = false)
    private String content;

    public static Comment of(final Long memberId, final Long reflectionId, final String content) {
        return new Comment(null, memberId, reflectionId, content);
    }
}
