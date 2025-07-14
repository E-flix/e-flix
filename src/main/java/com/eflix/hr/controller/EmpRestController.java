package com.eflix.hr.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.eflix.common.res.ResUtil;
import com.eflix.common.res.result.ResResult;
import com.eflix.common.res.result.ResStatus;
import com.eflix.common.security.auth.AuthUtil;
import com.eflix.hr.dto.DepartmentDTO;
import com.eflix.hr.dto.EmployeeDTO;
import com.eflix.hr.dto.GradeDTO;
import com.eflix.hr.dto.RoleDTO;
import com.eflix.hr.dto.etc.EmpSearchDTO;
import com.eflix.hr.service.DepartmentService;
import com.eflix.hr.service.EmployeeService;
import com.eflix.hr.service.GradeService;
import com.eflix.hr.service.RoleService;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/hr")
public class EmpRestController {

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private DepartmentService departmentService;

    @Autowired
    private GradeService gradeService;

    @Autowired
    private RoleService roleService;

    @Value("${upload.path}")
    private String path;

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
            searchResult.put("pageSize", empSearchDTO.getPageUnit());
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
        List<RoleDTO> roles = roleService.findAllByCoIdx(getCoIdx());
        if ((depts != null) || (grades != null)) {
            Map<String, Object> options = new HashMap<>();
            options.put("depts", depts);
            options.put("grades", grades);
            options.put("roles", roles);

            result = ResUtil.makeResult(ResStatus.OK, options);
        } else {
            result = ResUtil.makeResult("404", "데이터가 존재하지 않습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/emp/insert")
    public ResponseEntity<ResResult> postInsert(
            @RequestPart("empData") EmployeeDTO employeeDTO,
            @RequestPart(value = "empPhoto", required = false) MultipartFile empPhoto) throws IllegalStateException, IOException {

        ResResult result = null;

        employeeDTO.setCoIdx(getCoIdx());

        if (empPhoto != null) {
            String uploadDir = path + "/hr/emp/";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File dest = new File(uploadDir + empPhoto.getOriginalFilename());
            empPhoto.transferTo(dest);

            employeeDTO.setEmpImg(empPhoto.getOriginalFilename());
        }
        int affectedRows = employeeService.insert(employeeDTO);

        if (affectedRows > 0) {
            result = ResUtil.makeResult(ResStatus.OK, null);
        } else {
            result = ResUtil.makeResult("400", "등록 과정에서 오류가 발생했습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/emp/get")
    public ResponseEntity<ResResult> getEmp(@RequestParam("empIdx") String empIdx) {
        ResResult result = null;

        EmployeeDTO employeeDTO = employeeService.findByEmpIdx(empIdx);

        if (employeeDTO != null) {
            result = ResUtil.makeResult(ResStatus.OK, employeeDTO);
        } else {
            result = ResUtil.makeResult("404", "데이터가 존재하지 않습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PutMapping("/emp/update")
    public ResponseEntity<ResResult> putUpdate(
            @RequestPart("empData") EmployeeDTO employeeDTO,
            @RequestPart(value = "empPhoto", required = false) MultipartFile empPhoto) throws IllegalStateException, IOException {
        ResResult result = null;

        if (empPhoto != null) {
            String uploadDir = path + "/hr/emp/";
            File dir = new File(uploadDir);
            if (!dir.exists()) {
                dir.mkdirs();
            }
            File dest = new File(uploadDir + empPhoto.getOriginalFilename());
            empPhoto.transferTo(dest);

            employeeDTO.setEmpImg(empPhoto.getOriginalFilename());
        }
        employeeDTO.setCoIdx(getCoIdx());
        int affectedRows = employeeService.update(employeeDTO);

        if (affectedRows > 0) {
            result = ResUtil.makeResult(ResStatus.OK, null);
        } else {
            result = ResUtil.makeResult("400", "저장과정에서 오류가 발생했습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping("/emp/merge")
    public ResponseEntity<ResResult> postMerge(@RequestBody EmployeeDTO employeeDTO) {
        ResResult result = null;

        int affectedRows = employeeService.mergeEmployee(employeeDTO);

        if (affectedRows > 0) {
            result = ResUtil.makeResult(ResStatus.OK, null);
        } else {
            result = ResUtil.makeResult("400", "저장과정에서 오류가 발생했습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

}
