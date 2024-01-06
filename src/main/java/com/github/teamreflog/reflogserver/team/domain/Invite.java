package com.github.teamreflog.reflogserver.team.domain;

import com.github.teamreflog.reflogserver.common.entity.BaseEntity;
import com.github.teamreflog.reflogserver.team.domain.exception.UnauthorizedInviteException;
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
@Table(name = "invites")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Invite extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "team_id", nullable = false)
    private Long teamId;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    public static Invite of(final Long teamId, final Long memberId) {
        return new Invite(null, teamId, memberId);
    }

    public boolean isSameMember(final Long memberId) {
        return this.memberId.equals(memberId);
    }

    public void accept(final Long memberId) {
        if (!this.isSameMember(memberId)) {
            throw new UnauthorizedInviteException();
        }
    }

    public void reject(final Long memberId) {
        if (!this.isSameMember(memberId)) {
            throw new UnauthorizedInviteException();
        }
    }
}
