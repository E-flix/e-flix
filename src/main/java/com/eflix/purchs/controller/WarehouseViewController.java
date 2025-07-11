package com.eflix.purchs.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eflix.purchs.dto.WarehouseViewDTO;
import com.eflix.purchs.service.WarehouseViewService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Controller
@RequestMapping("/purchs")
public class WarehouseViewController {
    private final WarehouseViewService warehouseViewService;

    // 창고조회 페이지

        // 창고 조회
	@GetMapping("/whv")
	public String warehouse_view(Model model, WarehouseViewDTO warehouse) {
        model.addAttribute("warehouseViewList", warehouseViewService.warehouseViewList(null));
        model.addAttribute("whlocationList", warehouseViewService.searchWarehouseLocation());
		return "purchs/warehouse_view";
	}

    // 창고 조회 상세
    @GetMapping("/whvld")
    @ResponseBody
    public List<WarehouseViewDTO> warehouseViewListDetail(@RequestParam String warehouseId) {
        return warehouseViewService.warehouseViewListDetail(warehouseId);
    }
    
    @GetMapping("/whData")
    @ResponseBody
    public List<WarehouseViewDTO> getMethodName(WarehouseViewDTO warehouse) {
        return warehouseViewService.warehouseViewList(warehouse);
    }

    // @GetMapping("/whlList")
    // @ResponseBody
    // public List<WarehouseViewDTO> searchWarehouseLocation(WarehouseViewDTO warehouseLocation) {
    //     return warehouseViewService.searchWarehouseLocation();
    // }
    

    
    
    
}
