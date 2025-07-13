package com.eflix.purchs.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
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
	public String outbound() {
		return "purchs/outbound";
	}
    // 출고의뢰서 조회
    @GetMapping("/obr")
    @ResponseBody
    public List<OutboundDTO> selectOutboundRequest() {
        return outboundService.selectOutboundRequest();
    }
    // 출고의뢰서 상세 조회
    @GetMapping("/obrd")
    @ResponseBody
    public List<OutboundDTO> outboundRequestDetail(@RequestParam String outboundNo) {
        return outboundService.outboundRequestDetail(outboundNo);
    }
    // 출고 프로시저
    @PostMapping("/obp")
    @ResponseBody
    public List<OutboundDTO> outboundProcedure(@RequestBody OutboundDTO outboundDTO) {
        return outboundService.outboundProcedure(outboundDTO);
    }
    
}
