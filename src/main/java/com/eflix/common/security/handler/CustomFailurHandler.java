package com.eflix.common.security.handler;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// 최초생성 0703

public class CustomFailurHandler implements AuthenticationFailureHandler {
public CustomFailurHandler() {
    System.out.println("CustomFailurHandler created");
}
    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException exception) throws IOException, ServletException {
                
        String coIdx = request.getParameter("co_idx");

        if (coIdx != null && !coIdx.isEmpty()) {
            response.sendRedirect("/erp");
        } else {
            response.sendRedirect("/");
        }
    }
    
}
