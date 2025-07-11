package com.eflix.purchs.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.eflix.purchs.dto.OutboundDTO;
import com.eflix.purchs.service.OutboundService;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@RequiredArgsConstructor
@Controller
@RequestMapping("/purchs")
public class OutboundController {

    private final OutboundService outboundService;  
    // 출고관리
	@GetMapping("/obd")
	public String selectOutboundRequest(Model model) {
        model.addAttribute("outboundRequest", outboundService.selectOutboundRequest());
		return "purchs/outbound";
	}

    @GetMapping("/obrd")
    @ResponseBody
    public List<OutboundDTO> outboundRequestDetail(@RequestParam String outboundNo) {
        return outboundService.outboundRequestDetail(outboundNo);
    }
    
    
}
