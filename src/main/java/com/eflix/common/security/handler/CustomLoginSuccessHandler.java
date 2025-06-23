package com.eflix.common.security.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.eflix.common.security.details.CustomUserDetails;
import com.eflix.common.security.dto.CustomerUserDTO;
import com.eflix.common.security.dto.UserDTO;
import com.eflix.hr.dto.EmployeeDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * Spring Security 로그인 성공 핸들러
 * <p>
 * 로그인 성공 시 사용자 유형에 따라 세션 정보를 저장하고,
 * ERP 사용자와 일반 사용자에 대해 각각 다른 경로로 리다이렉트 처리합니다.
 * </p>
 *
 * <h3>기능 설명</h3>
 * <ul>
 *   <li>EmployeeDTO가 존재할 경우, 사원 로그인으로 판단하여 세션에 사원 정보 저장 후 루트("/")로 이동</li>
 *   <li>UserDTO가 존재할 경우, 일반 사용자 로그인으로 판단하여 세션에 사용자 정보 저장 후 "/erp"로 이동</li>
 *   <li>사용자 권한 정보(authorities)도 세션에 함께 저장</li>
 * </ul>
 *
 * @author 복성민 (bokseongmin@gmail.com)
 * @version 1.0
 * @since 2025-06-19
 *
 * @see
 *
 * @changelog
 * <ul>
 *   <li>2025-06-19: 최초 생성 (복성민)</li>
 *   <li>2025-06-21: 사원/사용자 유형 분기 및 세션 저장 처리 추가 (이름)</li>
 * </ul>
 */


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
