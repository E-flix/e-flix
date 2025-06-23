package com.eflix.hr.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eflix.hr.dto.AttendanceRecordDTO;
import com.eflix.hr.dto.DepartmentDTO;
import com.eflix.hr.dto.EmployeeDTO;
import com.eflix.hr.service.AttendanceRecordService;
import com.eflix.hr.service.DepartmentService;
import com.eflix.hr.service.EmployeeService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


/** ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-18
  - 설명     : 인사 컨트롤러
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-18 (김어진): 인사 메인 화면 및 각 기능별 화면 요청 처리 메소드 추가
  - 2025-06-19 (김어진): 각 기능별 화면 요청 처리 메소드 추가
============================================  */

@Controller
@RequestMapping("/hr")
public class HrController {

  @Autowired
  private EmployeeService service;

  @Autowired
  private DepartmentService departmentService;

  // 인사 메인 화면
  @GetMapping("")
  public String hrMain () {
    return "hr/hrMain";
  }

  // 사원 관리 화면
  @GetMapping("/el")
  public String empList(Model model) {
    List<EmployeeDTO> records = service.getAllEmployees();
    List<DepartmentDTO> depts = departmentService.findAllDepts();
    model.addAttribute("empList", records);
    model.addAttribute("depts", depts);
    return "hr/emp";
  }

  /** AJAX: 특정 부서(deptUpIdx)의 하위 부서 목록 JSON 리턴 */
    @GetMapping("/{deptUpIdx}/children")
    @ResponseBody
    public List<DepartmentDTO> children(@PathVariable String deptUpIdx) {
        return departmentService.findAllDeptsUP(deptUpIdx);
    }

  // 부서 등록 화면(관리자)
  @GetMapping("/da")
  public String deptAdd() {
      return "hr/deptAdd";
  }
  
  // 근태 현황 화면(사원)
  @GetMapping("/al")
  public String attdList() {
      return "hr/attdList";
  }
  
  // 근태 관리화면(관리자)
  @GetMapping("/am")
  public String attdMaList() {
      return "hr/attdManager";
  }
  
  // 근태 신청화면 (사원)
  @GetMapping("/attdAdd")
  public String attdAdd() {
      return "hr/attdAdd";
  }
  
  // 근태 신청승인 화면(관리자)
  @GetMapping("/attdApproval")
  public String attdApproval() {
      return "hr/attdApproval";
  }
  
  // 급여 계산 화면 
  @GetMapping("/sc")
  public String salaryCalculate() {
      return "hr/salaryCalculate";
  }
  
  // 급여 항목 화면
  @GetMapping("/si")
  public String salaryItem() {
      return "hr/salaryItem";
  }

  // 급여 명세서 화면
  @GetMapping("/sp")
  public String salaryPayslip() {
      return "hr/salaryPayslip";
  }
}
