package com.eflix.hr.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eflix.common.res.ResUtil;
import com.eflix.common.res.result.ResResult;
import com.eflix.common.res.result.ResStatus;
import com.eflix.common.security.auth.AuthUtil;
import com.eflix.hr.dto.DepartmentDTO;
import com.eflix.hr.dto.GradeDTO;
import com.eflix.hr.service.DepartmentService;
import com.eflix.hr.service.GradeService;

@RestController
@RequestMapping("/hr")
public class HrRestController {

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private GradeService gradeService;

    public String getCoIdx() {
        return AuthUtil.getCoIdx();
    }

    @GetMapping("/options")
    public ResponseEntity<ResResult> getMethodName(@RequestParam String param) {
        ResResult result = null;

        List<DepartmentDTO> dept = departmentService.findUpAllByCoIdx(getCoIdx());
        List<GradeDTO> grade = gradeService.findAllByCoIdx(getCoIdx());

        if(dept != null || grade != null) {
            Map<String, Object> data = new HashMap<>();
            data.put("dept", dept);
            data.put("grade", grade);
            result = ResUtil.makeResult(ResStatus.OK, data);
        } else {
            result = ResUtil.makeResult("404", "데이터가 존재하지 않습니다", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
