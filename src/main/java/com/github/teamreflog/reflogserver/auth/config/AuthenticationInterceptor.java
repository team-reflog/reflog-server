package com.github.teamreflog.reflogserver.auth.config;

import static org.springframework.http.HttpMethod.OPTIONS;

import com.github.teamreflog.reflogserver.auth.domain.Authorities;
import com.github.teamreflog.reflogserver.auth.domain.Token;
import com.github.teamreflog.reflogserver.auth.domain.TokenParser;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
@RequiredArgsConstructor
public class AuthenticationInterceptor implements HandlerInterceptor {

    private static final String TOKEN = "token";

    private final TokenParser tokenParser;

    // TODO: Refactor and test
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

        /* CORS preflight */
        if (OPTIONS.matches(request.getMethod())) {
            response.setStatus(HttpServletResponse.SC_ACCEPTED);
            return true;
        }

        final Token jwt = tokenParser.parse(request.getHeader(HttpHeaders.AUTHORIZATION));
        request.setAttribute(TOKEN, jwt);

        return true;
    }
}
