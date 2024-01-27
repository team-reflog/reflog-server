package com.github.teamreflog.reflogserver.reflection.domain;

import com.github.teamreflog.reflogserver.common.entity.BaseEntity;
import com.github.teamreflog.reflogserver.reflection.exception.ReflectionAlreadyExistException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDate;
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

    @Column(name = "reflection_date", nullable = false, updatable = false)
    private LocalDate reflectionDate;

    public static Reflection create(
            final long memberId,
            final long topicId,
            final String content,
            final LocalDate reflectionDate,
            final TeamData teamData) {
        if (!teamData.containsReflectionDay(reflectionDate.getDayOfWeek())) {
            throw new ReflectionAlreadyExistException();
        }

        return new Reflection(null, memberId, topicId, content, reflectionDate);
    }
}
