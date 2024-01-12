package com.github.teamreflog.reflogserver.auth.config;

import static org.springframework.http.HttpMethod.OPTIONS;

import com.github.teamreflog.reflogserver.auth.application.dto.AuthPrincipal;
import com.github.teamreflog.reflogserver.auth.domain.ClaimType;
import com.github.teamreflog.reflogserver.auth.domain.Jwt;
import com.github.teamreflog.reflogserver.auth.domain.JwtExtractor;
import com.github.teamreflog.reflogserver.auth.domain.JwtParser;
import com.github.teamreflog.reflogserver.auth.domain.JwtProvider;
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
    private final JwtParser jwtParser;
    private final JwtExtractor jwtExtractor;

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

        final String token = jwtExtractor.extract(request.getHeader(HttpHeaders.AUTHORIZATION));
        final Jwt jwt = jwtParser.parse(token);
        final Long memberId = Long.valueOf(jwt.getClaim(ClaimType.MEMBER_ID));
        request.setAttribute(AUTH_PRINCIPAL, new AuthPrincipal(memberId));

        return true;
    }
}
