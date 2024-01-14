package com.github.teamreflog.reflogserver.mail.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("단위 테스트: RandomSixDigitCodeGenerator")
class RandomSixDigitCodeGeneratorTest {

    RandomSixDigitCodeGenerator generator;

    @BeforeEach
    void setUp() {
        generator = new RandomSixDigitCodeGenerator();
    }

    @Test
    @DisplayName("6자리 인증 코드를 생성한다.")
    void generateSixDigitCode() {
        /* when */
        final Integer code = generator.generateCode();

        /* then */
        assertThat(code).isBetween(100000, 1000000);
    }
}
