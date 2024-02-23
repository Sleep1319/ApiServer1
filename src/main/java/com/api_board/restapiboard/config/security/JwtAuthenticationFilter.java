package com.api_board.restapiboard.config.security;

import com.api_board.restapiboard.config.security.guard.CustomUserDetails;
import com.api_board.restapiboard.config.security.guard.CustomUserDetailsService;
import com.api_board.restapiboard.config.token.TokenHelper;
import com.api_board.restapiboard.service.TokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import java.io.IOException;
import java.util.Optional;


@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends GenericFilterBean {
    private final CustomUserDetailsService userDetailsService;

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        extractToken(request).map(userDetailsService::loadUserByUsername).ifPresent(this::setAuthentication);
        chain.doFilter(request, response);
    }

    private void setAuthentication(CustomUserDetails userDetails) {
        SecurityContextHolder.getContext().setAuthentication(new CustomAuthenticationToken(userDetails, userDetails.getAuthorities()));
    }

    private Optional<String> extractToken(ServletRequest request) {
        return Optional.ofNullable(((HttpServletRequest) request).getHeader("Authorization"));
    }
}
