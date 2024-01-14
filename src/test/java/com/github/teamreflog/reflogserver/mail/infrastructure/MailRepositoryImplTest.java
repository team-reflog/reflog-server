package com.github.teamreflog.reflogserver.mail.infrastructure;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.github.teamreflog.reflogserver.common.exception.ReflogIllegalArgumentException;
import com.github.teamreflog.reflogserver.mail.domain.MailAuthData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("단위 테스트: MailRepositoryImpl")
class MailRepositoryImplTest {

    MailRepositoryImpl mailRepositoryImpl;

    @BeforeEach
    void setUp() {
        mailRepositoryImpl = new MailRepositoryImpl();
    }

    @Nested
    @DisplayName("메일 인증 정보를 저장할 때")
    class WhenSaveData {

        String id;

        @BeforeEach
        void setUp() {
            /* given */
            final MailAuthData data = MailAuthData.of("reflog@email.com", 240114);

            /* when */
            id = mailRepositoryImpl.save(data);

            /* then */
            assertThat(id).isNotNull();
        }

        @Test
        @DisplayName("식별자에 해당하는 메일 인증 정보가 없으면 예외가 발생한다.")
        void throwExceptionWhenNotExist() {
            /* given */
            final String notExistId = id + "💩";

            /* when & then */
            assertThatCode(() -> mailRepositoryImpl.getById(notExistId))
                    .isInstanceOf(ReflogIllegalArgumentException.class);
        }
    }
}
