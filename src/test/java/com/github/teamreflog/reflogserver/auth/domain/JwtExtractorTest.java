package com.github.teamreflog.reflogserver.auth.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.github.teamreflog.reflogserver.auth.exception.JwtInvalidException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("단위 테스트: JwtExtractor")
class JwtExtractorTest {

    JwtExtractor extractor;

    @BeforeEach
    void setUp() {
        extractor = new JwtExtractor();
    }

    @Nested
    @DisplayName("토큰을 추출할 때")
    class WhenExtract {

        @Test
        @DisplayName("토큰이 Bearer로 시작하면 토큰을 추출한다.")
        void extractBearerToken() {
            /* given */
            final String bearerToken = "Bearer ekfrEjrqhRdlaktdlTrpTek";

            /* when */
            final String token = extractor.extract(bearerToken);

            /* then */
            assertThat(token).isEqualTo("ekfrEjrqhRdlaktdlTrpTek");
        }

        @Test
        @DisplayName("토큰 타입이 Bearer가 아니면 예외가 발생한다.")
        void throwExceptionWithWrongJwtType() {
            /* given */
            final String noType = "ekfrEjrqhRdlaktdlTrpTek";
            final String wrongType = "Wrong ekfrEjrqhRdlaktdlTrpTek";

            /* when */
            assertAll(
                    () ->
                            assertThatCode(() -> extractor.extract(noType))
                                    .isInstanceOf(JwtInvalidException.class)
                                    .hasMessage("유효하지 않은 토큰입니다."),
                    () ->
                            assertThatCode(() -> extractor.extract(wrongType))
                                    .isInstanceOf(JwtInvalidException.class)
                                    .hasMessage("유효하지 않은 토큰입니다."));
        }
    }
}
