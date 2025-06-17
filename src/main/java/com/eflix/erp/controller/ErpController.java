package com.eflix.erp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/erp")
public class ErpController {
	
	@GetMapping()
	public String getMethodName() {
		return "erp/index";
	}
}
