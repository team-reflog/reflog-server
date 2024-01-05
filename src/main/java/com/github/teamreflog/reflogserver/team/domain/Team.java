package com.github.teamreflog.reflogserver.team.domain;

import com.github.teamreflog.reflogserver.common.entity.BaseEntity;
import com.github.teamreflog.reflogserver.team.domain.exception.TeamReflectionDaysEmptyException;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "teams")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Team extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Column(name = "description", nullable = false)
    private String description;

    @Column(name = "owner_id", nullable = false)
    private Long ownerId;

    @Convert(converter = DayOfWeekListConverter.class)
    @Column(name = "reflection_days", nullable = false)
    private List<DayOfWeek> reflectionDays = new ArrayList<>();

    private Team(
            final String name,
            final String description,
            final Long ownerId,
            final List<DayOfWeek> reflectionDays) {
        this.name = name;
        this.description = description;
        this.ownerId = ownerId;
        this.reflectionDays = reflectionDays;
    }

    public static Team of(
            final String name,
            final String description,
            final Long ownerId,
            final List<DayOfWeek> reflectionDays) {
        if (reflectionDays.isEmpty()) {
            throw new TeamReflectionDaysEmptyException();
        }

        return new Team(name, description, ownerId, reflectionDays);
    }

    public boolean isOwner(final Long ownerId) {
        return this.ownerId.equals(ownerId);
    }

    public boolean containsReflectionDay(final DayOfWeek dayOfWeek) {
        return reflectionDays.contains(dayOfWeek);
    }
}
