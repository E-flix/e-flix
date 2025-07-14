package com.eflix.purchs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
/**
 * ============================================
 * - 작성자 : 이혁진
 * - 최초작성 : 2025-06-18
 * - 설명 : 재고 컨트롤러
 * -----------------------------------------------
 * [ 변경 이력 ]
 * - 2025-06-18 (이혁진): 재고 메인 화면 처리
 * - 2025-06-20 (이혁진): 화면 Controller 와 기능 Controller분리
 * ============================================
 */
@Controller
@RequestMapping("/purchs")
public class PurchsController {
	// 재고관리 목록
	@GetMapping("")
	public String category() {
		return "purchs/category";
	}
	
	// 재고조회
	@GetMapping("/ivyv")
	public String inventory_view() {
		return "purchs/inventory_view";
	}
}