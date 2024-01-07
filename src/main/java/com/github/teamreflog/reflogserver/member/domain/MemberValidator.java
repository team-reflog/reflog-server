package com.github.teamreflog.reflogserver.member.domain;

import com.github.teamreflog.reflogserver.auth.exception.PasswordNotMatchedException;
import com.github.teamreflog.reflogserver.member.domain.exception.EmailDuplicatedException;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MemberValidator {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    public void validateEmailDuplicated(final String email) {
        validateEmailDuplicated(memberRepository.findByEmail(new MemberEmail(email)));
    }

    void validateEmailDuplicated(final Optional<Member> member) {
        member.ifPresent(
                ignore -> {
                    throw new EmailDuplicatedException();
                });
    }

    public void validatePassword(final Member member, final String request) {
        validatePassword(passwordEncoder.matches(request, member.getPassword()));
    }

    void validatePassword(final boolean isMatched) {
        if (!isMatched) {
            throw new PasswordNotMatchedException();
        }
    }
}
