package com.github.teamreflog.reflogserver.team.domain;

import com.github.teamreflog.reflogserver.common.entity.BaseEntity;
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
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "teams")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
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

    public static Team of(
            final String name,
            final String description,
            final Long ownerId,
            final List<DayOfWeek> reflectionDays) {
        return new Team(null, name, description, ownerId, reflectionDays);
    }
}