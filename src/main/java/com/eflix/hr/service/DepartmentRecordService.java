/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 부서 이력 관리 Service
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): 인터페이스 생성
============================================ */
package com.eflix.hr.service;

import java.util.List;

import com.eflix.hr.dto.DepartmentRecordDTO;

public interface DepartmentRecordService {
    public List<DepartmentRecordDTO> getAllDepartmentRecords();
    public DepartmentRecordDTO getDepartmentRecordById(String deptHistId);
    int createDepartmentRecord(DepartmentRecordDTO dto);
    int updateDepartmentRecord(DepartmentRecordDTO dto);
    int deleteDepartmentRecord(String deptHistId);
}