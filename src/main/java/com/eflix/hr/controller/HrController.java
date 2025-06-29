package com.eflix.hr.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eflix.hr.dto.DepartmentDTO;
import com.eflix.hr.dto.EmployeeDTO;
import com.eflix.hr.dto.HrWorkTypeDTO;
import com.eflix.hr.service.DepartmentService;
import com.eflix.hr.service.EmployeeService;
import com.eflix.hr.service.HrWorkTypeService;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



/** ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-18
  - 설명     : 인사 컨트롤러
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-18 (김어진): 인사 메인 화면 및 각 기능별 화면 요청 처리 메소드 추가
  - 2025-06-19 (김어진): 각 기능별 화면 요청 처리 메소드 추가
  - 2025-06-23 (김어진): 사원조회 화면 요청 처리 메소드 추가
  - 2025-06-23 (김어진): 사원등록 화면 요청 처리 메소드 추가
============================================  */

@Controller
@RequestMapping("/hr")
public class HrController {

  @Autowired
  private EmployeeService employeeService;

  @Autowired
  private DepartmentService departmentService;

  @Autowired
  private HrWorkTypeService hrWorkTypeService;

  // 인사 메인 화면
  @GetMapping("")
  public String hrMain () {
    return "hr/hrMain";
  }

  // 사원 관리 화면
  @GetMapping("/el")
  public String empList(
    @RequestParam(value = "option", required=false) String option, 
    @RequestParam(value = "keyword", required=false) String keyword, 
    @RequestParam(required=false) String deptUpIdx,      
    @RequestParam(required=false) String deptIdx,        
    @RequestParam(required=false) String empGrade,       
    @RequestParam(required=false) String empStatusType,  
    @RequestParam(required=false) String empType,        
    Model model) {

    // 부서 드롭다운용 전체 부서 조회
    List<DepartmentDTO> depts = departmentService.findAllDepts();

    // 사원관리 페이지 직급 드롭다운 조회
    List<EmployeeDTO> gradeList = employeeService.gradeList();

    // 근무유형 드롭다운 조회
    List<HrWorkTypeDTO> workList = hrWorkTypeService.workList();
    
    // 재직상태 드롭다운 조회
    List<EmployeeDTO> empStatusList = employeeService.empStatusList();

    Map<String,Object> params = new HashMap<>();
    params.put("option", option);
    params.put("keyword", keyword);
    params.put("deptUpIdx", deptUpIdx);
    params.put("deptIdx", deptIdx);
    params.put("empGrade", empGrade);
    params.put("empStatusType", empStatusType);
    params.put("empType", empType);

    System.out.println(params.toString());
    
    List<EmployeeDTO> records = employeeService.getAllEmployees(params);

    // 뷰에 넘길 모델
    model.addAttribute("empList",  records);
    model.addAttribute("depts",     depts);
    model.addAttribute("option",    option);
    model.addAttribute("keyword",   keyword);
    model.addAttribute("gradeList",   gradeList);
    model.addAttribute("workList",   workList);
    model.addAttribute("empStatusList",   empStatusList);
    model.addAllAttributes(params);
    return "hr/emp";
  }

  /** AJAX: 특정 부서(deptUpIdx)의 하위 부서 목록 JSON 리턴 */
    @GetMapping("/{deptUpIdx}/children")
    @ResponseBody
    public List<DepartmentDTO> children(@PathVariable String deptUpIdx) {
        return departmentService.findAllDeptsUP(deptUpIdx);
    }

  //사원 등록
  @PostMapping("/insertEmp")
  @ResponseBody
  public String insertEmp(EmployeeDTO emp) {
    System.out.println(emp.toString());
    int affectedRows = employeeService.insertEmp(emp);
    if(affectedRows > 0) {
      return "200";
    } else {
      return "404";
    }
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
