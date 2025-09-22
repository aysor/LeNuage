package ru.netology.cloudservice.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import ru.netology.cloudservice.model.entity.UserEntity;
import ru.netology.cloudservice.security.JwtTokenProvider;
import ru.netology.cloudservice.service.AuthService;

import java.io.IOException;
import java.util.Collections;
import java.util.Optional;

//@RequiredArgsConstructor
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Value(value = "${jwt.header}")
    private String headerName;

    private final AuthService authService;
    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(final AuthService authService, final JwtTokenProvider jwtTokenProvider) {
        this.authService = authService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws IOException, ServletException {

        String token = jwtTokenProvider.resolveToken(request.getHeader(headerName));
        if(token != null) {
            String userName = jwtTokenProvider.getUsernameFromToken(token);
            Optional<UserEntity> userEntity = authService.getUserByName(userName);

            if (!userEntity.isEmpty()
                    && jwtTokenProvider.validateToken(token, userEntity.get())
                    && SecurityContextHolder.getContext().getAuthentication() == null) {

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userEntity, null, Collections.emptyList());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }
        filterChain.doFilter(request, response);
    }

}