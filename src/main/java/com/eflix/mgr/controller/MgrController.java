package com.eflix.mgr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;



@Controller
@RequestMapping("/mgr")
public class MgrController {
    
    @GetMapping("/user")
    public String getEmp() {
        return "mgr/user";
    }

    @GetMapping("/role")
    public String getRole() {
        return "mgr/role";
    }
    
    
}
