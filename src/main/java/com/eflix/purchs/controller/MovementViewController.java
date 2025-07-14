package com.eflix.purchs.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eflix.purchs.dto.MovementViewDTO;
import com.eflix.purchs.service.MovementViewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.ResponseBody;


@RequiredArgsConstructor
@Controller
@RequestMapping("/purchs")
public class MovementViewController {
    private final MovementViewService service;
    	// 창고이동추적
	@GetMapping("/wmv")
	public String warehouseMovementList(Model model) {
        model.addAttribute("warehouseViewList", service.warehouseMovementList(null));
        model.addAttribute("prodNameList", service.searchProdName());
		return "purchs/movement_view";
	}

    @GetMapping("/mvData")
    @ResponseBody
    public List<MovementViewDTO> warehouseMovementList(MovementViewDTO warehouseView) {
        return service.warehouseMovementList(warehouseView);
    }
    

}
