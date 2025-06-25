package com.eflix.purchs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eflix.purchs.dto.WarehouseDTO;
import com.eflix.purchs.service.WarehouseService;

import lombok.RequiredArgsConstructor;

/**
 * ============================================
 * - 작성자 : 이혁진
 * - 최초작성 : 2025-06-18
 * - 설명 : 재고 기능 컨트롤러
 * -----------------------------------------------
 * [ 변경 이력 ]
 * - 2025-06-20 (이혁진): 화면 Controller 와 기능 Controller 분리, 삽입기능 완성, 삭제기능 진행중
 * - 2025-06-23 (이혁진): 마지막 창고번호 가져오기 추가
 * ============================================
 */
@RequiredArgsConstructor
@Controller
@RequestMapping("/purchs")
public class WarehouseController {
	private final WarehouseService warehouseService;

	// 창고관리 - 조회
	@GetMapping("/wh")
	public String warehouse(Model model) {
		model.addAttribute("whList", warehouseService.getWarehouse());
		return "purchs/warehouse";
	}

	// 마지막 창고번호 가져오기
	@GetMapping("/whNexTid")
	@ResponseBody
	public String getNextWarehouseId() {
		return warehouseService.getNextWarehouseId();
	}

	// 창고관리 - 등록
	@PostMapping("/whc")
	@ResponseBody
	public WarehouseDTO warehouseInsert(@RequestBody WarehouseDTO warehouse) {
		warehouseService.insertWarehouse(warehouse);
		return warehouse;
	}

	// 창고관리 - 삭제
	@DeleteMapping("/whd")
	@ResponseBody
	public int warehouseDelete(@RequestBody WarehouseDTO warehouse) {
		return warehouseService.deleteWarehouse(warehouse);
	}
}