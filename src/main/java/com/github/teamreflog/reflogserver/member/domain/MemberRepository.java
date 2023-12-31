package com.github.teamreflog.reflogserver.member.domain;

import java.util.Optional;
import org.springframework.data.repository.Repository;

public interface MemberRepository extends Repository<Member, Long> {

    Optional<Member> findByEmail(MemberEmail email);

    Member save(Member member);

    boolean existsByEmail(MemberEmail email);
}
