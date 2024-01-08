package com.github.teamreflog.reflogserver.team.infrastructure;

import com.github.teamreflog.reflogserver.common.exception.ReflogIllegalArgumentException;
import com.github.teamreflog.reflogserver.member.domain.MemberEmail;
import com.github.teamreflog.reflogserver.member.domain.MemberRepository;
import com.github.teamreflog.reflogserver.team.domain.MemberQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberClient implements MemberQueryService {

    private final MemberRepository memberRepository;

    @Override
    public Long getIdByEmail(final String email) {
        return memberRepository
                .findByEmail(new MemberEmail(email))
                .orElseThrow(ReflogIllegalArgumentException::new)
                .getId();
    }
}
