package com.github.teamreflog.reflogserver.auth.application;

import static org.assertj.core.api.Assertions.assertThatCode;

import com.github.teamreflog.reflogserver.auth.application.dto.LoginRequest;
import com.github.teamreflog.reflogserver.auth.exception.PasswordNotMatchedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.context.jdbc.Sql.ExecutionPhase;

@Sql(scripts = "/clear.sql", executionPhase = ExecutionPhase.AFTER_TEST_METHOD)
@SpringBootTest
@DisplayName("통합 테스트: AuthService")
class AuthServiceTest {

    @Autowired AuthService authService;

    @Test
    @Sql("/member.sql")
    @DisplayName("비밀번호가 일치하지 않으면 로그인에 실패한다.")
    void loginFailWithNotMatchedPassword() {
        /* given */
        final LoginRequest request = new LoginRequest("maesil@email.com", "notmatched");

        /* when & then */
        assertThatCode(() -> authService.login(request))
                .isExactlyInstanceOf(PasswordNotMatchedException.class)
                .hasMessage("비밀번호가 일치하지 않습니다.");
    }
}
