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

import com.eflix.hr.dto.DepartmentsDTO;

public interface DepartmentsService {
    public List<DepartmentsDTO> getAllDepartments();
    public DepartmentsDTO getDepartmentById(String deptIdx);
    int createDepartment(DepartmentsDTO dto);
    int updateDepartment(DepartmentsDTO dto);
    int deleteDepartment(String deptIdx);
}
