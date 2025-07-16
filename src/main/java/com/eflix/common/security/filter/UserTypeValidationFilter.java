package com.eflix.common.security.filter;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@Component
public class UserTypeValidationFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        
        HttpSession session = request.getSession(false);
        if (session != null) {
            String userType = (String) session.getAttribute("userType");
            String uri = request.getRequestURI();

            if (userType != null) {
                if (userType.equals("ERP_MAIN") && uri.startsWith("/erp")) {
                    session.invalidate();
                    response.sendRedirect("/");
                    return;
                }

                if ((userType.equals("ERP_EMP") || userType.equals("ERP_MASTER"))
                        && (uri.equals("/") || uri.startsWith("/main") || uri.startsWith("/signup"))) {
                    session.invalidate();
                    response.sendRedirect("/erp/login");
                    return;
                }
            }
        }

        filterChain.doFilter(request, response);
    }
    
}
