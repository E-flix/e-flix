// package com.eflix.bsn.controller;

// import com.eflix.bsn.dto.SalesOutboundDTO;
// import com.eflix.bsn.dto.SoutboundDetailDTO;
// import com.eflix.bsn.service.SOutboundService;
// import org.springframework.web.bind.annotation.*;

// import java.util.List;

// @RestController
// @RequestMapping("/bsn/soutbounds")  // ★ 변경: /api/soutbounds → /bsn/soutbounds
// public class SOutboundController {

//     private final SOutboundService service;

//     public SOutboundController(SOutboundService service) {
//         this.service = service;
//     }

//     @GetMapping
//     public List<SalesOutboundDTO> list() {
//         return service.getOutboundList();
//     }

//     // ★ 수정: 경로 패턴 변경
//     @GetMapping("/{outboundNo}/details")  // URL 경로로 받도록 변경
//     public List<SoutboundDetailDTO> details(@PathVariable String outboundNo) {
//         return service.getOutboundDetails(outboundNo);
//     }

//     @PostMapping
//     public String create(@RequestBody SalesOutboundDTO dto) {
//         return service.createOutbound(dto);
//     }
// }