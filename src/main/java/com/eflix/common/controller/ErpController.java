package com.eflix.common.controller;

import java.util.Collection;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.GetMapping;

// 최초 생성 6 27
@Controller
public class ErpController {

    @GetMapping("/erp")
    public String main(Authentication authentication) {

        Collection<? extends GrantedAuthority> roles = authentication.getAuthorities();

        boolean isMaster = roles.stream().anyMatch(r -> r.getAuthority().equals("ROLE_MASTER"));
        boolean hasHRorACC = roles.stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_HR") || r.getAuthority().equals("ROLE_ACC"));
        boolean hasBNZorPURCHS = roles.stream()
                .anyMatch(r -> r.getAuthority().equals("ROLE_BNZ") || r.getAuthority().equals("ROLE_PURCHS"));

        if (isMaster)
            return "erp/hapb"; // 전체 보기
        if (hasHRorACC && hasBNZorPURCHS)
            return "erp/hapb"; // 모든 모듈
        if (hasHRorACC)
            return "erp/ha";
        if (hasBNZorPURCHS)
            return "erp/pb";
        return "error/404";
    }
}
