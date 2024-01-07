package com.github.teamreflog.reflogserver.member.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.Mockito.mock;

import com.github.teamreflog.reflogserver.member.domain.exception.EmailDuplicatedException;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("단위 테스트: MemberValidator")
class MemberValidatorTest {

    MemberValidator memberValidator;

    @BeforeEach
    void setUp() {
        memberValidator = new MemberValidator(mock(MemberRepository.class));
    }

    @Nested
    @DisplayName("중복 이메일이 있는지 검사할 때")
    class WhenValidateEmailDuplicated {

        @Test
        @DisplayName("동일한 이메일을 가진 멤버가 없으면 예외를 발생시키지 않는다.")
        void notThrowExceptionWithMemberNotExists() {
            /* given */
            final Optional<Member> member = Optional.empty();

            /* when & then */
            assertThatCode(() -> memberValidator.validateEmailDuplicated(member))
                    .doesNotThrowAnyException();
        }

        @Test
        @DisplayName("동일한 이메일을 가진 멤버가 있으면 예외를 발생시킨다.")
        void throwExceptionWithMemberExists() {
            /* given */
            final Optional<Member> member = Optional.of(Member.of("reflog@email.com", "reflog"));

            /* when & then */
            assertThatCode(() -> memberValidator.validateEmailDuplicated(member))
                    .isExactlyInstanceOf(EmailDuplicatedException.class)
                    .hasMessage("이미 사용중인 이메일입니다.");
        }
    }
}
