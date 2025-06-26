/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 부서 ServiceImpl
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): 클래스 생성
  - 2025-06-23 (김어진): 조건별 조회 구현
  - 2025-06-23 (김어진): 부서등록구현
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
  private DepartmentMapper departmentMapper;

  @Override
  public List<DepartmentDTO> getAllDepartments() {
    throw new UnsupportedOperationException("Unimplemented method 'getAllDepartments'");
  }

  @Override
  public DepartmentDTO getDepartmentById(String deptIdx) {
    throw new UnsupportedOperationException("Unimplemented method 'getDepartmentById'");
  }

  @Override
  public int createDepartment(DepartmentDTO dto) {
    throw new UnsupportedOperationException("Unimplemented method 'createDepartment'");
  }

  @Override
  public int updateDepartment(DepartmentDTO dto) {
    throw new UnsupportedOperationException("Unimplemented method 'updateDepartment'");
  }

  @Override
  public int deleteDepartment(String deptIdx) {
    throw new UnsupportedOperationException("Unimplemented method 'deleteDepartment'");
  }

  @Override
  public List<DepartmentDTO> findAllDepts() {
    return departmentMapper.findAllDepts();
  }

  @Override
  public List<DepartmentDTO> findAllDeptsUP(String deptIdx) {
    return departmentMapper.findAllDeptsUp(deptIdx);
  }

  // 부서등록
  // @Override
  // public int insertDept(DepartmentDTO dept) {
  //   return departmentMapper.insertDept(dept);
  // }
}
