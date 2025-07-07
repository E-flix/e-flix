package com.eflix.bsn.controller;

import com.eflix.bsn.dto.SalesOutboundDTO;
import com.eflix.bsn.dto.SoutboundDetailDTO;
import com.eflix.bsn.service.SOutboundService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/soutbounds")
public class SOutboundController {

    private final SOutboundService service;

    public SOutboundController(SOutboundService service) {
        this.service = service;
    }

    @GetMapping
    public List<SalesOutboundDTO> list() {
        return service.getOutboundList();
    }

    @GetMapping("/details")
    public List<SoutboundDetailDTO> details(@RequestParam String outboundNo) {
        return service.getOutboundDetails(outboundNo);
    }

    @PostMapping
    public String create(@RequestBody SalesOutboundDTO dto) {
        return service.createOutbound(dto);
    }
}
