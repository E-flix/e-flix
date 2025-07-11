package com.eflix.common.security.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

// 2025-06-26 최초 생성

@Slf4j
public class CustomLogoutSuccessHandler implements LogoutSuccessHandler{

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
            throws IOException, ServletException {
        String uri = request.getRequestURI();
        log.info("로그아웃 성공 - URI: {}", uri);
        
        if(uri.startsWith("/erp")) {
            response.sendRedirect("/erp");
        } else {
            response.sendRedirect("/");
        }
    }
    
}
