/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 부서 이력 관리 ServiceImpl
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): 클래스 생성
============================================ */
package com.eflix.hr.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eflix.hr.dto.DepartmentRecordDTO;
import com.eflix.hr.service.DepartmentRecordService;

@Service
public class DepartmentRecordServiceImpl implements DepartmentRecordService{

  @Override
  public List<DepartmentRecordDTO> getAllDepartmentRecords() {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getAllDepartmentRecords'");
  }

  @Override
  public DepartmentRecordDTO getDepartmentRecordById(String deptHistId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'getDepartmentRecordById'");
  }

  @Override
  public int createDepartmentRecord(DepartmentRecordDTO dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'createDepartmentRecord'");
  }

  @Override
  public int updateDepartmentRecord(DepartmentRecordDTO dto) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'updateDepartmentRecord'");
  }

  @Override
  public int deleteDepartmentRecord(String deptHistId) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException("Unimplemented method 'deleteDepartmentRecord'");
  }

}
