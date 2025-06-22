package com.eflix.erp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * ERP 회사 관리를 위한 Service 클래스
 * 
 * <p>
 * 회사 등록, 정보 관리, 사업자 등록증 처리 및 회사 마스터 계정 관리 등의 
 * 비즈니스 로직을 처리하는 ERP 컨트롤러입니다. 
 * ERP 시스템 사용을 위한 회사 정보 전반을 관리하는 기능을 제공합니다.
 * </p>
 * 
* <h3>주요 기능</h3>
 * <ul>
 *   <li>회사 등록 및 사업자등록번호 검증 화면 제공</li>
 *   <li>회사 정보 조회 및 수정 화면 제공</li>
 *   <li>사업자 등록증 업로드 관련 처리</li>
 *   <li>회사 마스터 계정 생성 및 서비스 상태 확인</li>
 *   <li>서비스 상태: 구독 전 / 구독 중 / 구독 만료 구분</li>
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
@RequestMapping("/erp")
public class ErpController {
	
	@GetMapping()
	public String home() {
		return "erp/index";
	}

	@GetMapping("/check")
	public String getMethodName() {
		return "erp/check";
	}

	@GetMapping("/pay")
	public String pay(@RequestParam("type") String type, Model model) {
		model.addAttribute("type", type);
		return "erp/pay";
	}

	@GetMapping("/mypage_info")
	public String mypage_info() {
		return "erp/mypageInfo";
	}

	@GetMapping("/mypage_service")
	public String mypage_service() {
		return "erp/mypageService";
	}
}