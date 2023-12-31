package com.github.teamreflog.reflogserver.auth.service;

import static org.assertj.core.api.Assertions.assertThatCode;

import com.github.teamreflog.reflogserver.auth.dto.LoginRequest;
import com.github.teamreflog.reflogserver.auth.exception.EmailNotExistException;
import com.github.teamreflog.reflogserver.auth.exception.PasswordNotMatchedException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.jdbc.Sql;

@DataJpaTest
@Import(AuthService.class)
@DisplayName("통합 테스트: AuthService")
class AuthServiceTest {

    @Autowired AuthService authService;

    @Test
    @DisplayName("이메일이 존재하지 않으면 로그인에 실패한다.")
    void loginFailWithNotExistEmail() {
        /* given */
        final LoginRequest request = new LoginRequest("notexist@email.com", "password");

        /* when & then */
        assertThatCode(() -> authService.login(request))
                .isExactlyInstanceOf(EmailNotExistException.class)
                .hasMessage("이메일이 존재하지 않습니다.");
    }

    @Test
    @Sql("/member.sql")
    @DisplayName("비밀번호가 일치하지 않으면 로그인에 실패한다.")
    void loginFailWithNotMatchedPassword() {
        /* given */
        final LoginRequest request = new LoginRequest("test@email.com", "notmatched");

        /* when & then */
        assertThatCode(() -> authService.login(request))
                .isExactlyInstanceOf(PasswordNotMatchedException.class)
                .hasMessage("비밀번호가 일치하지 않습니다.");
    }
}
