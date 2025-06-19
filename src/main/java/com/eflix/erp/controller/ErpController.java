package com.eflix.erp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;


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

	@GetMapping("/mypage_info")
	public String mypage_info() {
		return "erp/mypageInfo";
	}

	@GetMapping("/mypage_service")
	public String mypage_service() {
		return "erp/mypageService";
	}

	@GetMapping("/info_check")
	public String info_check() {
		return "erp/infoCheck";
	}
	
}
