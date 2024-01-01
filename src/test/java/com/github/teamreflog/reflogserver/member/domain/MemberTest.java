package com.github.teamreflog.reflogserver.member.domain;

import static org.assertj.core.api.Assertions.assertThatCode;

import com.github.teamreflog.reflogserver.member.exception.EmailFormatException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("단위 테스트: Member")
class MemberTest {

    @Test
    @DisplayName("이메일 형식이 올바르지 않으면 생성에 실패한다.")
    void createFailWithInvalidEmail() {
        /* given */
        final String wrongEmail = "reflog!@#!@#ASD";

        /* when & then */
        assertThatCode(() -> Member.of(wrongEmail, "reflog"))
                .isExactlyInstanceOf(EmailFormatException.class)
                .hasMessage("이메일 형식이 올바르지 않습니다.");
    }
}
