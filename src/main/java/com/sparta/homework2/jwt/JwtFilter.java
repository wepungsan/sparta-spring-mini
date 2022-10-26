package com.sparta.homework2.jwt;

import com.sparta.homework2.dto.TokenDto;
import com.sparta.homework2.model.RefreshToken;
import com.sparta.homework2.repository.RefreshTokenRepository;
import com.sparta.homework2.service.AuthService;
import io.jsonwebtoken.ExpiredJwtException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Slf4j
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
    private final TokenProvider tokenProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        // 1. Request Header 에서 토큰을 꺼냄
        String accessToken = resolveAccessToken(request);
        String refreshToken = resolveRefreshToken(request);

        // 2. validateToken 으로 토큰 유효성 검사
        // accessToken 검사
        if(StringUtils.hasText(accessToken) && tokenProvider.validateToken(accessToken)) {
            Authentication authentication = tokenProvider.getAuthentication(accessToken);
            SecurityContextHolder.getContext().setAuthentication(authentication);
            log.info("1. JWT Access 토큰이 통과되었습니다.");
        }

        // refreshToken 검사
        if(StringUtils.hasText(refreshToken) && tokenProvider.validateToken(refreshToken)) {
            log.info("2. JWT Refresh 토큰이 통과되었습니다.");
        }

        filterChain.doFilter(request, response);
    }
    
    // Request Header 에서 토큰 정보를 꺼내오기
    private String resolveAccessToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    private String resolveRefreshToken(HttpServletRequest request) {
        String refreshToken = request.getHeader("RefreshToken");
        return refreshToken;
    }
}
