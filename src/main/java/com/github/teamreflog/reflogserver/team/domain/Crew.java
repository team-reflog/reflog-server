package com.github.teamreflog.reflogserver.team.domain;

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
@Table(name = "crews")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class Crew extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "member_id", nullable = false)
    private Long memberId;

    @Column(name = "nickname", nullable = false)
    private String nickname;

    public static Crew of(final Long memberId, final String nickname) {
        return new Crew(null, memberId, nickname);
    }

    public boolean isSameNickname(final String nickname) {
        return this.nickname.equals(nickname);
    }

    public boolean isSameMemberId(final Long memberId) {
        return this.memberId.equals(memberId);
    }
}
