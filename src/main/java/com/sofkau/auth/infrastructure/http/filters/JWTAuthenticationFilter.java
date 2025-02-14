package com.sofkau.auth.infrastructure.http.filters;

import com.sofkau.auth.application.exceptions.token.NotValidTokenFoundException;
import com.sofkau.auth.domain.entities.Token;
import com.sofkau.auth.domain.services.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;

import java.io.IOException;
import java.util.Collections;

@Component
@RequiredArgsConstructor
public class JWTAuthenticationFilter extends OncePerRequestFilter {
    private final TokenService tokenService;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain chain) throws ServletException, IOException {
        try {
            if (SecurityContextHolder.getContext().getAuthentication() != null) {
                chain.doFilter(request, response);
                return;
            }

            String token = getTokenFromRequest(request);

            if (token == null) {
                this.logger.trace("No valid token found in Authorization header");
                chain.doFilter(request, response);
                return;
            }

            Token storedToken = tokenService.getToken(token);

            if (storedToken.isExpired()) {
                tokenService.revokeToken(storedToken);
                throw new NotValidTokenFoundException();
            }

            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    storedToken.getCustomer().getId(),
                    storedToken.getAccessToken(),
                    Collections.emptyList()
            );

            authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);

            chain.doFilter(request, response);
        } catch (Exception e) {
            if (e instanceof NotValidTokenFoundException)
                this.logger.trace("No valid token found in Authorization header");

            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String authorization = request.getHeader(HttpHeaders.AUTHORIZATION);

        if (authorization == null ||
                !StringUtils.startsWithIgnoreCase(authorization, "Bearer"))
            return null;

        String jwtToken = authorization.substring(7).trim();

        if (jwtToken.equalsIgnoreCase("Bearer"))
            throw new BadCredentialsException("Empty bearer authentication token");

        return jwtToken;
    }
}
