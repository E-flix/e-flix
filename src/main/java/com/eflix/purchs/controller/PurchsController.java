package com.eflix.purchs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eflix.purchs.service.WarehouseService;

import lombok.RequiredArgsConstructor;


//입고페이지
@RequiredArgsConstructor
@Controller
@RequestMapping("/purchs") public class PurchsController {

	// 재고관리목록
	@GetMapping("/category") public String category() {
		return "purchs/category";
	}

	// 입고관리
	@GetMapping("/inbound") public String inbound() {
		return "purchs/inbound";
	}

	// 출고관리
	@GetMapping("/outbound") public String outbound() {
		return "purchs/outbound";
	}

	private final WarehouseService warehouseService;
	// 창고관리
	@GetMapping("/warehouse") public String warehouse(Model model) {
		model.addAttribute("whList", warehouseService.getWarehouse());
		return "purchs/warehouse";
	}

	// 창고이동
	@GetMapping("/warehouse_movement") public String warehouse_mnt() {
		return "purchs/warehouse_movement";
	}

	// 입고조회
	@GetMapping("/inbound_view") public String inbound_view() {
		return "purchs/inbound_view";
	}

	// 출고조회
	@GetMapping("/outbound_view") public String outbound_view() {
		return "purchs/outbound_view";
	}

	// 창고조회
	@GetMapping("warehouse_view") public String warehouse_view() {
		return "purchs/warehouse_view";
	}

	// 재고조회
	@GetMapping("inventory_view") public String inventory_view() {
		return "purchs/inventory_view";
	}
}