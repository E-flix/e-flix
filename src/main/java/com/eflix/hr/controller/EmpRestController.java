package com.eflix.hr.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eflix.common.res.ResUtil;
import com.eflix.common.res.result.ResResult;
import com.eflix.common.res.result.ResStatus;
import com.eflix.common.security.auth.AuthUtil;
import com.eflix.hr.dto.DepartmentDTO;
import com.eflix.hr.dto.EmployeeDTO;
import com.eflix.hr.dto.GradeDTO;
import com.eflix.hr.dto.etc.EmpSearchDTO;
import com.eflix.hr.service.DepartmentService;
import com.eflix.hr.service.EmployeeService;
import com.eflix.hr.service.GradeService;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;



@RestController
@RequestMapping("/hr")
public class EmpRestController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private GradeService gradeService;

    public String getCoIdx() {
        return AuthUtil.getCoIdx();
    }

    // 0708
    @GetMapping("/emp/list")
    public ResponseEntity<ResResult> getList(EmpSearchDTO empSearchDTO) {
        ResResult result = null;

        empSearchDTO.setCoIdx(getCoIdx());

        int empCount = employeeService.findAllEmpCount(empSearchDTO);

        empSearchDTO.setTotalRecord(empCount);

        List<EmployeeDTO> list = employeeService.findAllEmployeeSearch(empSearchDTO);

        if (list != null) {
            Map<String, Object> searchResult = new HashMap<>();
            searchResult.put("emps", list);
            searchResult.put("total", empCount);
            searchResult.put("page", empSearchDTO.getPage());
            searchResult.put("startPage", empSearchDTO.getStartPage());
            searchResult.put("endPage", empSearchDTO.getEndPage());
            searchResult.put("lastPage", empSearchDTO.getLastPage());
            result = ResUtil.makeResult(ResStatus.OK, searchResult);
        } else {
            result = ResUtil.makeResult("404", "데이터가 존재하지 않습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 0709
    @GetMapping("/emp/searchOptions")
    public ResponseEntity<ResResult> getSearchOptions() {
        ResResult result = null;

        List<DepartmentDTO> depts = departmentService.findUpAllByCoIdx(getCoIdx());
        List<GradeDTO> grades = gradeService.findAllByCoIdx(getCoIdx());
        if ((depts != null) || (grades != null)) {
            Map<String, Object> options = new HashMap<>();
            options.put("depts", depts);
            options.put("grades", grades);

            result = ResUtil.makeResult(ResStatus.OK, options);
        } else {
            result = ResUtil.makeResult("404", "데이터가 존재하지 않습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
    @PostMapping("/emp/update")
    public ResponseEntity<ResResult> postMethodName(@RequestBody EmployeeDTO employeeDTO) {
        ResResult result = null;

        int affectedRows = employeeService.updateEmployee(employeeDTO);

        if(affectedRows > 0) {
            result = ResUtil.makeResult(ResStatus.OK, null);
        } else {
            result = ResUtil.makeResult("400", "저장과정에서 오류가 발생했습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
}
