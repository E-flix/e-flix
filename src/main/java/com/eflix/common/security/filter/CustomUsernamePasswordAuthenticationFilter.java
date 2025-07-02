package com.eflix.common.security.filter;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomUsernamePasswordAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    
    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {

        String username = obtainUsername(request);
        String password = obtainPassword(request);
        String coIdx = request.getParameter("co_idx");
        String masterCheck = request.getParameter("masterChecked");

        UsernamePasswordAuthenticationToken authRequest =
            new UsernamePasswordAuthenticationToken(username, password);

        // 커스텀 정보를 RequestContext에 저장
        request.setAttribute("co_idx", coIdx);
        if(masterCheck != null) {
            request.setAttribute("masterChecked", masterCheck);
        } else {
            request.setAttribute("masterChecked", "off");
        }

        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        // SecurityContext에 Authentication 설정
        SecurityContextHolder.getContext().setAuthentication(authResult);
        
        // 세션에 SecurityContext 저장
        HttpSession session = request.getSession();
        session.setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
        
        log.info("=== 인증 성공 ===");
        System.out.println("인증 정보: " + authResult);
        System.out.println("사용자 정보: " + authResult.getPrincipal());
        
        // 부모 클래스의 성공 처리 로직 호출 (SuccessHandler 실행 등)
        super.successfulAuthentication(request, response, chain, authResult);
    }
}
