// package com.eflix.purchs.controller;

// import java.util.List;

// import org.springframework.stereotype.Controller;
// import org.springframework.ui.Model;
// import org.springframework.web.bind.annotation.RequestMapping;

// import com.eflix.purchs.dto.OutboundViewDTO;
// import com.eflix.purchs.service.OutboundViewService;
// import lombok.RequiredArgsConstructor;
// import org.springframework.web.bind.annotation.GetMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.bind.annotation.ResponseBody;

// @RequiredArgsConstructor
// @Controller
// @RequestMapping("/purchs")
// public class OutboundViewController {

//     private final OutboundViewService service;

//     // 출고결과조회
//     @GetMapping("/obdv")
//     @ResponseBody
//     public String outboundViewList(Model model, OutboundViewDTO DTO) {
//         model.addAttribute("obdView", service.outboundViewList(null));
//         model.addAttribute("searchId", service.searchOutboundId());
//         return "purchs/outbound_view";
//     }
//     // 출고결과상세조회
//     @GetMapping("/obdViewDetail")
//     @ResponseBody
//     public List<OutboundViewDTO> outboundViewDetailList(@RequestParam String outboundId) {
//         return service.outboundViewDetailList(outboundId);
//     }
    
// }
