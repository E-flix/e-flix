/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 사원 ServiceImpl
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): 클래스 생성
============================================ */
package com.eflix.hr.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eflix.hr.dto.DepartmentDTO;
import com.eflix.hr.dto.EmployeeDTO;
import com.eflix.hr.mapper.EmployeeMapper;
import com.eflix.hr.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService{

  @Autowired
  EmployeeMapper employeeMapper;

  @Override
  public List<EmployeeDTO> getAllEmployees() {
    return employeeMapper.selectAll();
  }

  @Override
  public EmployeeDTO getEmployeeById(String empIdx) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getEmployeeById'");
  }

  @Override
  public int createEmployee(EmployeeDTO dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'createEmployee'");
  }

  @Override
  public int updateEmployee(EmployeeDTO dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'updateEmployee'");
  }

  @Override
  public int deleteEmployee(String empIdx) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteEmployee'");
  }

}
