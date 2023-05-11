package com.wallet.cardservice.interceptor;

import com.wallet.cardservice.util.SecurityRegistrar;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;

@Configuration
@RequiredArgsConstructor
public class RequestSecurityAuthorizationInterceptor extends OncePerRequestFilter {

    private static final String AUTHORIZATION_HEADER = "Authorization";

    private final SecurityRegistrar securityRegistrar;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest,
                                    HttpServletResponse httpServletResponse,
                                    FilterChain filterChain) throws ServletException, IOException {
        Enumeration<String> headerNames = httpServletRequest.getHeaderNames();
        Iterator<String> stringIterator = headerNames.asIterator();
        while (stringIterator.hasNext()) {
            String header = stringIterator.next();
            if (header.equals(AUTHORIZATION_HEADER.toLowerCase(Locale.ROOT))) {
                String token = httpServletRequest.getHeader(AUTHORIZATION_HEADER);
                securityRegistrar.registerUserFromTokenToSecurityContext(token);
                httpServletResponse.addHeader(AUTHORIZATION_HEADER, token);
                break;
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }
}
