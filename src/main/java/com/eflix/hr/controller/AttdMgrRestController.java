package com.eflix.hr.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eflix.hr.service.AttendanceRecordService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/hr/attd/mgr")
public class AttdMgrRestController {
    
    @Autowired
    private AttendanceRecordService attendanceRecordService;

    @GetMapping("/")
    public String getMethodName(@RequestParam String param) {
        return new String();
    }
    
}
