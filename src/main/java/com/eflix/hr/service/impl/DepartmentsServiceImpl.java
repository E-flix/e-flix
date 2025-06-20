/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 부서 ServiceImpl
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): 클래스 생성
============================================ */
package com.eflix.hr.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eflix.hr.dto.DepartmentsDTO;
import com.eflix.hr.service.DepartmentsService;

@Service
public class DepartmentsServiceImpl implements DepartmentsService{

  @Override
  public List<DepartmentsDTO> getAllDepartments() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getAllDepartments'");
  }

  @Override
  public DepartmentsDTO getDepartmentById(String deptIdx) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getDepartmentById'");
  }

  @Override
  public int createDepartment(DepartmentsDTO dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'createDepartment'");
  }

  @Override
  public int updateDepartment(DepartmentsDTO dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'updateDepartment'");
  }

  @Override
  public int deleteDepartment(String deptIdx) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteDepartment'");
  }

}
