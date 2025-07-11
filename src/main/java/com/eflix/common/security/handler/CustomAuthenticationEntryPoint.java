package com.eflix.common.security.handler;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;


// 최초 생성 6/26
@Slf4j
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
        String uri = request.getRequestURI();
		log.info("비인증 접근 시도: {}", uri);

		if (uri.startsWith("/erp")) {
			response.sendRedirect("/erp/login");
		} else if (uri.startsWith("/hr") || uri.startsWith("/acc")
		        || uri.startsWith("/purchs") || uri.startsWith("/bnz") || uri.startsWith("/mgr")) {
			response.sendRedirect("/erp/login");
		} else {
			// 그 외 기본 리다이렉트
			response.sendRedirect("/");
		}
    }
    
}
