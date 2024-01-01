package com.github.teamreflog.reflogserver.auth.config;

import static org.springframework.http.HttpMethod.OPTIONS;

import com.github.teamreflog.reflogserver.auth.dto.AuthPrincipal;
import com.github.teamreflog.reflogserver.auth.infrastructure.JwtProvider;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

    private static final String AUTH_PRINCIPAL = "authPrincipal";

    private final JwtProvider jwtProvider;

    @Override
    public boolean preHandle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler)
            throws Exception {
        if (OPTIONS.matches(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            return true;
        }

        final String token = jwtProvider.extractToken(request.getHeader(HttpHeaders.AUTHORIZATION));
        final Long memberId = jwtProvider.parseSubject(token);
        request.setAttribute(AUTH_PRINCIPAL, new AuthPrincipal(memberId));

        return true;
    }
}
