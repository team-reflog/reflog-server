package com.github.teamreflog.reflogserver.reflection.domain;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

import com.github.teamreflog.reflogserver.common.exception.ReflogIllegalArgumentException;
import com.github.teamreflog.reflogserver.reflection.exception.CommentNotAccessException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

@DisplayName("단위 테스트: CommentValidator")
class CommentValidatorTest {

    CommentValidator commentValidator;
    CrewQueryClient crewQueryClient;

    @BeforeEach
    void setUp() {
        crewQueryClient = mock(CrewQueryClient.class);
        commentValidator = new CommentValidator(crewQueryClient);
    }

    @Nested
    @DisplayName("댓글 권한이 있는지 검증할 때")
    class WhenValidateAccess {

        @Test
        @DisplayName("회원이 회고의 팀의 크루가 아닌 경우 예외가 발생한다")
        void throwExceptionWhenMemberIsNotCrewOfReflection() {
            /* given */
            given(crewQueryClient.getCrewDataByMemberIdAndReflectionId(any(), any()))
                    .willThrow(ReflogIllegalArgumentException.class);

            /* when & then */
            assertThatCode(() -> commentValidator.validateAccess(1L, 1L))
                    .isInstanceOf(CommentNotAccessException.class);
        }
    }
}
