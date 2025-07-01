package com.eflix.purchs.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;

import com.eflix.purchs.service.WhMovementService;


/**
 * ============================================
 * - 작성자 : 이혁진
 * - 최초작성 : 2025-06-18
 * - 설명 : 창고이동 기능 컨트롤러
 * -----------------------------------------------
 * [ 변경 이력 ]
 * - 2025-07-01 (이혁진): 
 * ============================================
 */
@RequiredArgsConstructor
@Controller
@RequestMapping("/purchs")
public class WhMovementController {
    private final WhMovementService whMovementService;
    // 창고이동
	@GetMapping("/whm")
	public String warehouse_mnt(Model model) {
        // 제품명 조회
        model.addAttribute("prdList", whMovementService.searchProdIdList());
		return "purchs/warehouse_movement";
	}
    
}
