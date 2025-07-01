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
import org.springframework.stereotype.Service;

import com.eflix.common.security.auth.AuthContext;
import com.eflix.hr.dto.EmployeeDTO;
import com.eflix.hr.mapper.EmployeeMapper;
import com.eflix.hr.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService{

  @Autowired
  EmployeeMapper employeeMapper;

  @Autowired
  private AuthContext authContext;
  
  // // 사원관리 페이지 검색조건 드롭다운 조회
  // @Override
  // public List<EmployeeDTO> getAllEmployees(String option, String keyword) {
  //   return employeeMapper.selectAll(option, keyword);
  // }

  @Override
  public EmployeeDTO selectById(String empIdx) {
    // throw new UnsupportedOperationException("Unimplemented method 'getEmployeeById'");
    return employeeMapper.selectById(authContext.getCoIdx(), empIdx);
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
        params.put("coIdx", authContext.getCoIdx());
        return employeeMapper.selectAll(params);
    }

  // 사원등록 
    @Override
    public int insertEmp(EmployeeDTO emp) {
      return employeeMapper.insertEmp(emp);
    }
}
