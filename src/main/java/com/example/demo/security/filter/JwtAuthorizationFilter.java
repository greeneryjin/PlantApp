package com.example.demo.security.filter;

import com.example.demo.headerconfig.ResponseResult;
import com.example.demo.security.token.JwtProperties;
import com.example.demo.security.token.JwtUsernamePasswordAuthenticationToken;
import com.example.demo.security.user.AccountContext;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//인증 완료 후, 계속 인가 처리.
@Slf4j
public class JwtAuthorizationFilter  extends OncePerRequestFilter {

    ResponseResult result = new ResponseResult();
    private JwtProperties jwtProperties;

    public JwtAuthorizationFilter(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response
            , FilterChain chain) throws IOException, ServletException {

        log.info("<<<<<<<<<<<<< 현재 JwtAuthorizationFilter  >>>>>>>>>>>>");
        // Jwt 헤더 정보 가지고 오기
        String authorizationHeader = request.getHeader(JwtProperties.HEADER_STRING);

        // 인증이 필요하지 않는 URL
        if (authorizationHeader == null || !authorizationHeader.startsWith(JwtProperties.TOKEN_PREFIX)) {
            request.setAttribute("exception", HttpStatus.NOT_FOUND.toString());
            chain.doFilter(request, response);
        }

        //URL 토큰 유효성 검사
        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {

        }
    }
}