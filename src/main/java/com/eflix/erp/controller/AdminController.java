package com.eflix.erp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * ERP 회사 관리를 위한 Service 클래스
 * 
 * <p>
 * ERP 관리자 기능을 제공하는 컨트롤러입니다.
 * 회사 내부 관리자(Admin) 전용으로, 시스템 설정 및 사용자 관리, 조직 구성 관리,
 * 직원 계정 관리 등의 화면을 제공합니다.
 * </p>
 * 
* <h3>주요 기능</h3>
 * <ul>
 *   <li>관리자 홈 대시보드</li>
 *   <li>CS 부서/직원 관리</li>
 *   <li>CM (조직 또는 고객사?) 관리</li>
 *   <li>사용자 계정 조회 및 관리</li>
 *   <li>임직원 목록 조회 및 관리</li>
 * </ul>
 * 
 * @author 복성민 (bokseongmin@gmail.com)
 * @version 1.0
 * @since 2025-06-18
 * 
 * @see
 * 
 * @changelog
 * <ul>
 *   <li>2025-06-18: 최초 생성 (복성민)</li>
 * </ul>
 */

@Controller
@RequestMapping("/erp/admin")
public class AdminController {

    @GetMapping()
    public String home() {
        return "erp/admin/home";
    }
    
    @GetMapping("/cs/list")
    public String csList() {
        return "erp/admin/pages/emp/list";
    }

    @GetMapping("/cm/list")
    public String cmList() {
        return "erp/admin/pages/cm/list";
    }

    @GetMapping("/user/list")
    public String spList() {
        return "erp/admin/pages/user/list";
    }

    @GetMapping("/emp/list")
    public String empList() {
        return "erp/admin/pages/emp/list";
    }
}
