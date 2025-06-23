/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 부서 Service
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): 인터페이스 생성
============================================ */
package com.eflix.hr.service;

import java.util.List;

import com.eflix.hr.dto.DepartmentDTO;

public interface DepartmentService {
    public List<DepartmentDTO> getAllDepartments();
    public DepartmentDTO getDepartmentById(String deptIdx);
    int createDepartment(DepartmentDTO dto);
    int updateDepartment(DepartmentDTO dto);
    int deleteDepartment(String deptIdx);
}
