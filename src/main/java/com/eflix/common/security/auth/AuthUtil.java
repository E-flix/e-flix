package com.eflix.common.security.auth;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.eflix.common.security.details.SecurityUserDetails;
import com.eflix.common.security.dto.SecurityEmpDTO;
import com.eflix.common.security.dto.SecurityMasterDTO;

public class AuthUtil {

    private AuthUtil() {
        // 유틸 클래스이므로 생성자 막기
    }

    private static SecurityUserDetails getUserDetails() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated() || auth instanceof AnonymousAuthenticationToken) {
            return null;
        }

        Object principal = auth.getPrincipal();
        if (principal instanceof SecurityUserDetails userDetails) {
            return userDetails;
        }

        return null;
    }

    public static String getCoIdx() {
        SecurityUserDetails userDetails = getUserDetails();
        if (userDetails != null && userDetails.getSecurityEmpDTO() != null) {
            return userDetails.getSecurityEmpDTO().getCoIdx();
        } else if (userDetails != null && userDetails.getSecurityMasterDTO() != null) {
            return userDetails.getSecurityMasterDTO().getCoIdx();
        }
        return "co-101"; // 기본값
    }

    public static String getMstIdx() {
        SecurityUserDetails userDetails = getUserDetails();
        if (userDetails != null && userDetails.getSecurityMasterDTO() != null) {
            return userDetails.getSecurityMasterDTO().getMstId();
        }
        return "mst-101"; // 기본값
    }

    public static String getEmpIdx() {
        SecurityUserDetails userDetails = getUserDetails();
        if (userDetails != null && userDetails.getSecurityEmpDTO() != null) {
            return userDetails.getSecurityEmpDTO().getEmpIdx();
        }
        return "emp-101"; // 기본값
    }

    public static SecurityMasterDTO getLoginMasterDTO() {
        SecurityUserDetails userDetails = getUserDetails();
        if (userDetails != null) {
            return userDetails.getSecurityMasterDTO();
        }
        return null;
    }

    public static SecurityEmpDTO getLoginEmployeeDTO() {
        SecurityUserDetails userDetails = getUserDetails();
        if (userDetails != null) {
            return userDetails.getSecurityEmpDTO();
        }
        return null;
    }
}
