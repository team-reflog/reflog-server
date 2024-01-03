package com.github.teamreflog.reflogserver.team.domain;

import com.github.teamreflog.reflogserver.common.entity.BaseEntity;
import com.github.teamreflog.reflogserver.invite.domain.Invite;
import com.github.teamreflog.reflogserver.team.exception.NicknameDuplicateException;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.ConstraintMode;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @JoinColumn(name = "team_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private final List<TeamMember> members = new ArrayList<>();

    @OneToMany(
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY,
            mappedBy = "team")
    private final List<Invite> invites = new ArrayList<>();

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
        return new Team(name, description, ownerId, reflectionDays);
    }

    public boolean isOwner(final Long ownerId) {
        return this.ownerId.equals(ownerId);
    }

    public void addInvite(final Invite entity) {
        invites.add(entity);
    }

    public void processInvite(final Invite invite, final String nickname) {
        if (members.stream().anyMatch(member -> member.isSameNickname(nickname))) {
            throw new NicknameDuplicateException();
        }

        members.add(TeamMember.of(this.id, invite.getMemberId(), nickname));
        invites.remove(invite);
    }
}
