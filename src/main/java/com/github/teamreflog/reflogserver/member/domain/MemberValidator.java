package com.github.teamreflog.reflogserver.member.domain;

import com.github.teamreflog.reflogserver.member.domain.exception.EmailDuplicatedException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberValidator {

    private final MemberRepository memberRepository;

    public void validateEmailDuplicated(final String email) {
        validateEmailDuplicated(memberRepository.findByEmail(new MemberEmail(email)));
    }

    void validateEmailDuplicated(final Optional<Member> member) {
        member.ifPresent(
                ignore -> {
                    throw new EmailDuplicatedException();
                });
    }
}
