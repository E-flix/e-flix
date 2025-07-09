package com.eflix.hr.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eflix.common.res.ResUtil;
import com.eflix.common.res.result.ResResult;
import com.eflix.common.res.result.ResStatus;
import com.eflix.common.security.auth.AuthUtil;
import com.eflix.hr.dto.EmployeeDTO;
import com.eflix.hr.service.DepartmentService;
import com.eflix.hr.service.EmployeeService;
import com.eflix.hr.service.GradeService;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/hr")
public class EmpRestController {

    @Autowired
    private EmployeeService employeeService;

    public String getCoIdx() {
        return AuthUtil.getCoIdx();
    }

    // 0708
    @GetMapping("/emp/list")
    public ResponseEntity<ResResult> getList(@RequestBody EmployeeDTO employeeDTO) {
        ResResult result = null;

        employeeDTO.setCoIdx(getCoIdx());

        List<EmployeeDTO> list = employeeService.findAllEmployee(employeeDTO);

        if(list != null) {
            result = ResUtil.makeResult(ResStatus.OK, list);
        } else {
            result = ResUtil.makeResult("404", "데이터가 존재하지 않습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
