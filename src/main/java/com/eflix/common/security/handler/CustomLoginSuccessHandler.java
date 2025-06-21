package com.eflix.common.security.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.eflix.common.security.details.CustomUserDetails;
import com.eflix.common.security.dto.CustomerUserDTO;
import com.eflix.common.security.dto.EmployeeDTO;
import com.eflix.common.security.dto.UserDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomLoginSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
            Authentication authentication) throws IOException, ServletException {
        // TODO Auto-generated method stub
        // throw new UnsupportedOperationException("Unimplemented method 'onAuthenticationSuccess'");
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        
        if (userDetails.getEmployeeDTO() != null) {
			EmployeeDTO empDTO = userDetails.getEmployeeDTO();

            request.getSession().setAttribute("empEmail", empDTO.getEmpEmail());
            request.getSession().setAttribute("empName", empDTO.getEmpName());
            request.getSession().setAttribute("coIdx", empDTO.getCoIdx());
            request.getSession().setAttribute("authorities", userDetails.getAuthorities());
			response.sendRedirect("/");
		} else {
			UserDTO userDTO = userDetails.getUserDTO();
			request.getSession().setAttribute("userId", userDTO.getUserId());
			request.getSession().setAttribute("userName", userDTO.getUserName());
			response.sendRedirect("/erp");
		}
    }

}
