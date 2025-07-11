package com.eflix.hr.controller;

import java.util.List;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eflix.common.res.ResUtil;
import com.eflix.common.res.result.ResResult;
import com.eflix.common.res.result.ResStatus;
import com.eflix.common.security.auth.AuthUtil;
import com.eflix.hr.dto.DepartmentDTO;
import com.eflix.hr.service.DepartmentService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/hr/dept")
public class DeptRestController {

    @Autowired
    private DepartmentService departmentService;

    public String getCoIdx() {
        return AuthUtil.getCoIdx();
    }
    
    @GetMapping("/list/up")
    public ResponseEntity<ResResult> getUpList() {
        ResResult result = null;

        List<DepartmentDTO> list = departmentService.findUpAllByCoIdx(getCoIdx());

        if(list != null) {
            result = ResUtil.makeResult(ResStatus.OK, list);
        } else {
            result = ResUtil.makeResult("404", "데이터가 존재하지 않습니다.", "");
        }
        
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/list/down")
    public ResponseEntity<ResResult> getDownList(@RequestParam("deptUpIdx") String deptUpIdx) {
        ResResult result = null;
        
        List<DepartmentDTO> list = departmentService.findDownAllByCoIdx(getCoIdx(), deptUpIdx);

        if(list != null) {
            result = ResUtil.makeResult(ResStatus.OK, list);
        } else {
            result = ResUtil.makeResult("404", "데이터가 존재하지 않습니다.", "");
        }
        
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/list/all")
    public List<DepartmentDTO> getUpList(@RequestParam("coIdx") String coIdx) {
        return departmentService.findAllDepts(coIdx);
    }
    
}
