package com.eflix.common.security.auth;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.eflix.common.security.details.SecurityUserDetails;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthContext {

    protected String empIdx = "emp-101"; // 기본값
    protected String coIdx = "co-101"; // 기본값
    protected String mstIdx = "mst-101"; // 기본값
    
    // 초기화 로직
    @PostConstruct
    public void initAuth() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        
        // Authentication 자체가 AnonymousAuthenticationToken 이면 비로그인
        if (auth == null || auth instanceof AnonymousAuthenticationToken) {
            coIdx = "co-101";
            log.info("로그인 안 함");
            return;
        }
        
        // principal 이 UserDetails 가 아니면 (익명 문자열인 경우) 비로그인
        Object principal = auth.getPrincipal();
        if (!(principal instanceof UserDetails)) {
            coIdx = "co-101";
            log.info("로그인 안 함 (principal is " + principal + ")");
            return;
        }
        
        // 로그인된 경우 - UserDetails에서 coIdx 추출
        SecurityUserDetails securityUserDetails = (SecurityUserDetails) principal;
        if(securityUserDetails.getSecurityEmpDTO() != null) {
            empIdx = securityUserDetails.getSecurityMasterDTO().getMstId();
            coIdx = securityUserDetails.getSecurityMasterDTO().getCoIdx();
            log.info("사원 로그인 - coIdx: " + coIdx);
        }
        if(securityUserDetails.getSecurityMasterDTO() != null) {
            mstIdx = securityUserDetails.getSecurityMasterDTO().getMstId();
            coIdx = securityUserDetails.getSecurityMasterDTO().getCoIdx();
            log.info("마스터 로그인 - coIdx: " + coIdx);
        }
    }

    public String getCoIdx() {
        return coIdx;
    }
}
