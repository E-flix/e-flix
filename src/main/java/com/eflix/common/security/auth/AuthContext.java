package com.eflix.common.security.auth;

import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AuthContext {

    protected String coIdx = "co-101"; // 기본값
    
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
        // UserDetails userDetails = (UserDetails) principal;
        // coIdx = extractCoIdxFromUserDetails(userDetails);
        coIdx = "co-101"; // 임시값
        log.info("로그인됨 - coIdx: " + coIdx);
    }

    public String getCoIdx() {
        return coIdx;
    }
}
