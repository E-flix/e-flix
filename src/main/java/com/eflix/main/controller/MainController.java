package com.eflix.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.eflix.common.security.auth.AuthUtil;
import com.eflix.common.security.details.SecurityUserDetails;
import com.eflix.main.dto.ModuleDTO;
import com.eflix.main.dto.SubscriptionPackageDTO;
import com.eflix.main.dto.UserDTO;
import com.eflix.main.dto.etc.SubscriptionInfoDTO;
import com.eflix.main.service.CompanyService;
import com.eflix.main.service.ModuleService;
import com.eflix.main.service.SubscriptionService;
import com.eflix.main.service.UserService;

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
 * <li>회사 등록 및 사업자등록번호 검증 화면 제공</li>
 * <li>회사 정보 조회 및 수정 화면 제공</li>
 * <li>사업자 등록증 업로드 관련 처리</li>
 * <li>회사 마스터 계정 생성 및 서비스 상태 확인</li>
 * <li>서비스 상태: 구독 전 / 구독 중 / 구독 만료 구분</li>
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
 *   <li>2025-06-23: 구독 정보 조회 로직 추가 (복성민)</li>
 * </ul>
 */

@Controller
@RequestMapping("/")
public class MainController {

	@Autowired
	private SubscriptionService subscriptionService;

	@Autowired
	private ModuleService moduleService;

	@Autowired
	private UserService userService;

	@Autowired
	private CompanyService companyService;

	@GetMapping()
	public String home() {
		
		return "index";
	}

	@GetMapping("/check")
	public String getMethodName() {
		return "main/check";
	}

	@GetMapping("/pay")
	public String pay(@RequestParam("spkIdx") String spkIdx, Model model) {
		SubscriptionPackageDTO subscriptionPackageDTO = subscriptionService.findById(spkIdx);

		List<ModuleDTO> moduleList = moduleService.findAll();

		model.addAttribute("subscriptionPackage", subscriptionPackageDTO);
		model.addAttribute("moduleList", moduleList);

		return "main/pay";
	}

	@GetMapping("/mypage")
	public String mypage() {
		return "main/mypages/check";
	}
	

	@GetMapping("/mypage/info")
	public String mypage_info() {
		return "main/mypages/info";
	}

	@GetMapping("/mypage/service")
	public String mypage_service() {
		return "main/mypages/service";
	}
}