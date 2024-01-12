package com.github.teamreflog.reflogserver.auth.config;

import com.github.teamreflog.reflogserver.auth.application.dto.AuthPrincipal;
import com.github.teamreflog.reflogserver.auth.domain.Authorities;
import com.github.teamreflog.reflogserver.auth.domain.MemberRole;
import com.github.teamreflog.reflogserver.auth.domain.Token;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthorizationInterceptor implements HandlerInterceptor {

    private static final String TOKEN = "token";
    private static final String AUTH_PRINCIPAL = "authPrincipal";

    @Override
    public boolean preHandle(
            final HttpServletRequest request,
            final HttpServletResponse response,
            final Object handler)
            throws Exception {
        final HandlerMethod handlerMethod = (HandlerMethod) handler;
        final Authorities authorities = handlerMethod.getMethodAnnotation(Authorities.class);
        if (authorities == null) {
            return true;
        }

        final Token token = (Token) request.getAttribute(TOKEN);
        for (final MemberRole role : authorities.roles()) {
            if (token.hasRole(role)) {
                request.setAttribute(AUTH_PRINCIPAL, new AuthPrincipal(token.getSubject()));

                return true;
            }
        }

        return false;
    }
}
