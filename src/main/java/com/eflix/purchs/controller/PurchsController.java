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

	// 재고관리목록
	@GetMapping("/cgy")
	public String category() {
		return "purchs/category";
	}

	// 출고관리
	@GetMapping("/obd")
	public String outbound() {
		return "purchs/outbound";
	}

	// 창고이동
	@GetMapping("/whm")
	public String warehouse_mnt() {
		return "purchs/warehouse_movement";
	}

	// 입고조회
	@GetMapping("/ibdv")
	public String inbound_view() {
		return "purchs/inbound_view";
	}

	// 출고조회
	@GetMapping("/obdv")
	public String outbound_view() {
		return "purchs/outbound_view";
	}

	// 창고조회
	@GetMapping("/whv")
	public String warehouse_view() {
		return "purchs/warehouse_view";
	}

	// 재고조회
	@GetMapping("/ivyv")
	public String inventory_view() {
		return "purchs/inventory_view";
	}
}