package com.eflix.purchs.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import com.eflix.purchs.dto.WhMovementDTO;
import com.eflix.purchs.service.WhMovementService;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

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
    public String fromWarehouse(Model model) {
        // 이동전 창고 목록
        model.addAttribute("fromWhList", whMovementService.fromWarehouse());
        // 선택한 이동 전 창고 안에 있는 제품 목록 + 수량
        // model.addAttribute("fromProdList", whMovementService.fromProd());
        return "purchs/warehouse_movement";
    }

    @GetMapping("/fromProd")
    public List<WhMovementDTO> fromProd(@RequestParam String warehouseId) {
        return whMovementService.fromProd(warehouseId);
    }

    @GetMapping("/toWarehouse")
    public List<WhMovementDTO> toWarehouse(@RequestParam String fromWhId) {
        return whMovementService.toWarehouse(fromWhId);
    }

    @PostMapping("/executeTransfer")
    @ResponseBody
    public ResponseEntity<Map<String, Object>> executeTransfer(@RequestBody WhMovementDTO dto) {
        Map<String, Object> response = new HashMap<>();
        try {
            whMovementService.executeWarehouseTransfer(dto);
            response.put("success", true);
            response.put("message", "창고 이동이 완료되었습니다.");
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(response);
        }
    }

    // @GetMapping("/whdm")
    // @ResponseBody
    // public List<WhMovementDTO> warehouse_mnt() {
    // // 제품명 조회
    // return whMovementService.searchProdIdList();
    // }
}
