package com.github.teamreflog.reflogserver.mail.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.github.teamreflog.reflogserver.mail.exception.MailAuthCodeNotMatchedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("단위 테스트: MailAuthData")
class MailAuthDataTest {

    MailAuthData data;

    @BeforeEach
    void setUp() {
        data = MailAuthData.of("reflog@email.com", 240114);
    }

    @Test
    @DisplayName("인증 번호를 검증한다.")
    void isMatchedCode() {
        /* given */
        final Integer correctCode = 240114;
        final Integer incorrectCode = 240115;

        /* when & then */
        assertAll(
                () -> assertThatCode(() -> data.verify(correctCode)).doesNotThrowAnyException(),
                () ->
                        assertThatCode(() -> data.verify(incorrectCode))
                                .isInstanceOf(MailAuthCodeNotMatchedException.class)
                                .hasMessage("인증 번호가 일치하지 않습니다."));
    }
}
