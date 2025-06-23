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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eflix.hr.dto.DepartmentDTO;
import com.eflix.hr.mapper.DepartmentMapper;
import com.eflix.hr.service.DepartmentService;

@Service
public class DepartmentServiceImpl implements DepartmentService{

  @Autowired
  private DepartmentMapper mapper;

  @Override
  public List<DepartmentDTO> getAllDepartments() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getAllDepartments'");
  }

  @Override
  public DepartmentDTO getDepartmentById(String deptIdx) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getDepartmentById'");
  }

  @Override
  public int createDepartment(DepartmentDTO dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'createDepartment'");
  }

  @Override
  public int updateDepartment(DepartmentDTO dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'updateDepartment'");
  }

  @Override
  public int deleteDepartment(String deptIdx) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteDepartment'");
  }

  @Override
  public List<DepartmentDTO> findAllDepts() {
    // TODO Auto-generated method stub
    // throw new UnsupportedOperationException("Unimplemented method 'findAllDepts'");
    return mapper.findAllDepts();
  }

  @Override
  public List<DepartmentDTO> findAllDeptsUP(String deptIdx) {
    // TODO Auto-generated method stub
    // throw new UnsupportedOperationException("Unimplemented method 'findAllDeptsUP'");
    return mapper.findAllDeptsUp(deptIdx);
  }
}
