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

import com.eflix.common.security.auth.AuthContext;
import com.eflix.common.security.auth.AuthUtil;
import com.eflix.hr.dto.DepartmentDTO;
import com.eflix.hr.mapper.DepartmentMapper;
import com.eflix.hr.service.DepartmentService;

@Service
public class DepartmentServiceImpl implements DepartmentService{

  @Autowired
  private DepartmentMapper departmentMapper;

  // 부서조회
  @Override
  public List<DepartmentDTO> selectAll(DepartmentDTO departmentDTO) {
    departmentDTO.setCoIdx(AuthUtil.getCoIdx());
    return departmentMapper.selectAll(departmentDTO);
  }

    // 부서등록
  @Override
  public int insertDept(DepartmentDTO dept) {
    dept.setCoIdx(AuthUtil.getCoIdx());
    return departmentMapper.insertDept(dept);
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
  public List<DepartmentDTO> findAllDepts(String coIdx) {
    return departmentMapper.findAllDepts(coIdx);
  }

  @Override
  public List<DepartmentDTO> findAllDeptsUP(String deptIdx) {
    return departmentMapper.findAllDeptsUp(AuthUtil.getCoIdx(), deptIdx);
  }

  @Override
  public List<DepartmentDTO> findUpAllByCoIdx(String coIdx) {
    return departmentMapper.findUpAllByCoIdx(coIdx);
  }

  @Override
  public List<DepartmentDTO> findDownAllByCoIdx(String coIdx, String deptUpIdx) {
    return departmentMapper.findDownAllByCoIdx(coIdx, deptUpIdx);
  }

  @Override
  public DepartmentDTO findByEmpIdx(String empIdx) {
    return departmentMapper.findByEmpIdx(empIdx);
  }
}
