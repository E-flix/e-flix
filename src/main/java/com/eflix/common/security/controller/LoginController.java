package com.eflix.common.security.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


/**
 * <p>
 * ERP 관리자 기능을 제공하는 컨트롤러입니다.
 * 회사 내부 관리자(Admin) 전용으로, 시스템 설정 및 사용자 관리, 조직 구성 관리,
 * 직원 계정 관리 등의 화면을 제공합니다.
 * </p>
 *
 * <h3>주요 기능</h3>
 * <ul>
 *   <li></li>
 * </ul>
 *
 * @author 
 * @since 2025.06.20
 *
 * @changelog
 * <ul>
 *   <li>2025-06-19: 최초 생성 (복성민)</li>
 * </ul>
 */

@Slf4j
@Controller
public class LoginController {
    
    @GetMapping("/login")
    public String login() {
        // Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return "pages/account/login";
    }
}
