package com.eflix.erp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
@RequestMapping("/erp/admin")
public class AdminController {

    @GetMapping()
    public String home() {
        return "erp/admin/home";
    }
    
    @GetMapping("/cs/list")
    public String csList() {
        return "erp/admin/pages/emp/list";
    }

    @GetMapping("/cm/list")
    public String cmList() {
        return "erp/admin/pages/cm/list";
    }

    @GetMapping("/user/list")
    public String spList() {
        return "erp/admin/pages/user/list";
    }

    @GetMapping("/emp/list")
    public String empList() {
        return "erp/admin/pages/emp/list";
    }
}
