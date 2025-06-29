package com.eflix.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;

// 최초 생성 6 27

@Controller
@RequestMapping("/erp")
public class ErpController {
    
    @GetMapping()
    public String main() {
        return "erp/index";
    }
    
}
