package com.eflix.hr.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.eflix.hr.dto.DepartmentDTO;
import com.eflix.hr.service.DepartmentService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/** ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-18
  - 설명     : 부서 컨트롤러
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-26 (김어진): HrDepartController 생성 
============================================  */

@Controller
@RequestMapping("/hr")
public class HrDepartController {

  @Autowired
  private DepartmentService departmentService;

  
  // 부서 등록 화면(관리자)
  // @GetMapping("/da")
  // public String deptAdd() {
  //     return "hr/deptAdd";
  // }

  // 부서조회
  @GetMapping("/da")
  public String deptList(DepartmentDTO dept, Model model) {
    List<DepartmentDTO> deptList = departmentService.selectAll();
    model.addAttribute("deptList", deptList);
      return "hr/deptAdd";
  }
  

  // 부서등록
  @PostMapping("/insertDept")
  @ResponseBody
  public String insertDept(@RequestBody DepartmentDTO dept) {
      int affectedRows = departmentService.insertDept(dept);
      if(affectedRows > 0) {
        return "200";
      } else {
        return "404";
      }
  }
  
}