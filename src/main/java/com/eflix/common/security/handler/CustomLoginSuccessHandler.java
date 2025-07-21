package com.eflix.common.security.handler;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.eflix.common.security.details.SecurityUserDetails;
import com.eflix.common.security.dto.SecurityEmpDTO;
import com.eflix.common.security.dto.SecurityMasterDTO;
import com.eflix.common.security.dto.SecurityUserDTO;

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

        SecurityUserDetails userDetails = (SecurityUserDetails) authentication.getPrincipal();

        // ERP 사원 로그인
        if (userDetails.getSecurityEmpDTO() != null) {
            SecurityEmpDTO securityEmpDTO = userDetails.getSecurityEmpDTO();

            request.getSession().setAttribute("empEmail", securityEmpDTO.getEmpEmail());
            request.getSession().setAttribute("empName", securityEmpDTO.getEmpName());
            request.getSession().setAttribute("coIdx", securityEmpDTO.getCoIdx());
            request.getSession().setAttribute("authorities", userDetails.getAuthorities());
            request.getSession().setAttribute("roleId", securityEmpDTO.getRoleId());
            request.getSession().setAttribute("empImg", securityEmpDTO.getEmpImg());
            request.getSession().setAttribute("userType", "ERP_EMP");

            log.info("ERP 사원 로그인 성공: {}", securityEmpDTO.getEmpEmail());

            request.getSession().setAttribute("authorities", userDetails.getAuthorities());
            response.sendRedirect("/erp");
        }

        // ERP 마스터 로그인
        else if (userDetails.getSecurityMasterDTO() != null) {
            SecurityMasterDTO masterDTO = userDetails.getSecurityMasterDTO();

            request.getSession().setAttribute("mstIdx", masterDTO.getMstIdx());
            request.getSession().setAttribute("mstName", masterDTO.getMstName());
            request.getSession().setAttribute("coIdx", masterDTO.getCoIdx());
            request.getSession().setAttribute("role", "ROLE_MASTER");
            request.getSession().setAttribute("userType", "ERP_MASTER");

            log.info("ERP 마스터 로그인 성공: {}", masterDTO.getMstId());

            request.getSession().setAttribute("authorities", userDetails.getAuthorities());
            response.sendRedirect("/erp");
        }

        // 메인 사용자 로그인
        else if (userDetails.getSecurityUserDTO() != null) {
            SecurityUserDTO securityUserDTO = userDetails.getSecurityUserDTO();

            request.getSession().setAttribute("userIdx", securityUserDTO.getUserIdx());
            request.getSession().setAttribute("userId", securityUserDTO.getUserId());
            request.getSession().setAttribute("userName", securityUserDTO.getUserName());
            request.getSession().setAttribute("coIdx", securityUserDTO.getCoIdx());
            request.getSession().setAttribute("userType", "ERP_MAIN");

            log.info("메인 사용자 로그인 성공: {}", securityUserDTO.getUserId());
            
            request.getSession().setAttribute("authorities", userDetails.getAuthorities());
            
            response.sendRedirect("/");
        }

        // 예외 또는 기본 홈으로
        else {
            log.warn("알 수 없는 사용자 유형 로그인 성공 처리");
            response.sendRedirect("/");
        }
    }

}
