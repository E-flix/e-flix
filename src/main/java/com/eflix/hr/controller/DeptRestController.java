package com.eflix.hr.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eflix.hr.dto.DepartmentDTO;
import com.eflix.hr.service.DepartmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/hr/dept")
public class DeptRestController {

    @Autowired
    private DepartmentService departmentService;
    
    @GetMapping("/list/up")
    public List<DepartmentDTO> getUpList(@RequestParam("coIdx") String coIdx) {
        return departmentService.findUpAllByCoIdx(coIdx);
    }

    @GetMapping("/list/down")
    public List<DepartmentDTO> getList(@RequestParam("coIdx") String coIdx, @RequestParam("deptUpIdx") String deptUpIdx) {
        return departmentService.findDownAllByCoIdx(coIdx, deptUpIdx);
    }
}
