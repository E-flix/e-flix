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

import com.eflix.hr.dto.EmployeesDTO;
import com.eflix.hr.mapper.EmployeeMapper;
import com.eflix.hr.service.EmployeesService;

@Service
public class EmployeesServiceImpl implements EmployeesService{

  @Autowired
  EmployeeMapper employeeMapper;

  @Override
  public List<EmployeesDTO> getAllEmployees() {
    return employeeMapper.selectAll();
  }

  @Override
  public EmployeesDTO getEmployeeById(String empIdx) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getEmployeeById'");
  }

  @Override
  public int createEmployee(EmployeesDTO dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'createEmployee'");
  }

  @Override
  public int updateEmployee(EmployeesDTO dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'updateEmployee'");
  }

  @Override
  public int deleteEmployee(String empIdx) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteEmployee'");
  }

}
