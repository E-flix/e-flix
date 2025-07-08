package com.eflix.purchs.controller;

import java.util.List;
import org.springframework.stereotype.Controller;
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
 * - 2025-07-01 (이혁진): 힘들다.
 * ============================================
 */
@RequiredArgsConstructor
@Controller
@RequestMapping("/purchs")
public class WhMovementController {
    private final WhMovementService whMovementService;

    // 창고이동 페이지
    @GetMapping("/whm")
    public String whMovement() {
        return "purchs/warehouse_movement";
    }

    @GetMapping("/fromWarehouse")
    @ResponseBody
    public List<WhMovementDTO> fromWarehouse() {
        return whMovementService.fromWarehouse();
    }
    

    @GetMapping("/fromProd")
    @ResponseBody
    public List<WhMovementDTO> fromProd(@RequestParam String fromWhId) {
        return whMovementService.fromProd(fromWhId);
    }

    @GetMapping("/toWarehouse")
    @ResponseBody
    public List<WhMovementDTO> toWarehouse(@RequestParam String toWhId) {
        return whMovementService.toWarehouse(toWhId);
    }

    @PostMapping("/executeTransfer")
    @ResponseBody
    public void executeTransfer(@RequestBody WhMovementDTO whMovementDTO) {
        whMovementService.executeWarehouseTransfer(whMovementDTO);
    }

    // @GetMapping("/whdm")
    // @ResponseBody
    // public List<WhMovementDTO> warehouse_mnt() {
    // // 제품명 조회
    // return whMovementService.searchProdIdList();
    // }
}
