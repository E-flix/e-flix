/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 사원 ServiceImpl
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): 클래스 생성
  - 2025-06-23 (김어진): 조건별 조회기능 구현
  - 2025-06-24 (김어진): 사원관리 드롭다운 구현
  - 2025-06-26 (김어진): 사원수정 구현
============================================ */
package com.eflix.hr.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.eflix.hr.dto.EmployeeDTO;
import com.eflix.hr.mapper.EmployeeMapper;
import com.eflix.hr.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService{

  @Autowired
  EmployeeMapper employeeMapper;

  private String coIdx = "co-101";

  public void initAuth() {
    Authentication auth = SecurityContextHolder.getContext().getAuthentication();

    // 1) Authentication 자체가 AnonymousAuthenticationToken 이면 비로그인
    if (auth == null || auth instanceof AnonymousAuthenticationToken) {
        // 비로그인 처리
        coIdx = "co-101";
        System.out.println("로그인 안 함");
        return;
    }

    // 2) principal 이 UserDetails 가 아니면 (익명 문자열인 경우) 비로그인
    Object principal = auth.getPrincipal();
    if (!(principal instanceof UserDetails)) {
        // 비로그인 처리
        System.out.println("로그인 안 함 (principal is " + principal + ")");
        return;
    }
  }
  
  // // 사원관리 페이지 검색조건 드롭다운 조회
  // @Override
  // public List<EmployeeDTO> getAllEmployees(String option, String keyword) {
  //   return employeeMapper.selectAll(option, keyword);
  // }

  @Override
  public EmployeeDTO selectById(String empIdx) {
    // throw new UnsupportedOperationException("Unimplemented method 'getEmployeeById'");
    return employeeMapper.selectById(coIdx, empIdx);
  }

  @Override
  public int createEmployee(EmployeeDTO dto) {
    throw new UnsupportedOperationException("Unimplemented method 'createEmployee'");
  }

  @Override
  public int updateEmployee(EmployeeDTO dto) {
    throw new UnsupportedOperationException("Unimplemented method 'updateEmployee'");
  }

  @Override
  public int deleteEmployee(String empIdx) {
    throw new UnsupportedOperationException("Unimplemented method 'deleteEmployee'");
  }

  // 사원관리 페이지 직급 드롭다운용 조회
  @Override
  public List<EmployeeDTO> gradeList() {
    return employeeMapper.gradeList();
  }

  // 재직 상태 드롭다운 조회
  @Override
  public List<EmployeeDTO> empStatusList() {
    return employeeMapper.empStatusList();
  }

    @Override
    public List<EmployeeDTO> getAllEmployees(Map<String, Object> params) {
        params.put("coIdx", coIdx);
        return employeeMapper.selectAll(params);
    }

  // 사원등록 
    @Override
    public int insertEmp(EmployeeDTO emp) {
      return employeeMapper.insertEmp(emp);
    }
}
