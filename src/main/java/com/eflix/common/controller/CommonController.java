package com.eflix.common.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequestMapping("/error")
public class CommonController {
    
    @GetMapping("/404")
    public String notFound() {
        return "pages/error/404";
    }
    
}
