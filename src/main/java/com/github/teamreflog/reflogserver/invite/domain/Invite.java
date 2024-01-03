package com.github.teamreflog.reflogserver.invite.domain;

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
}
