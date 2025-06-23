/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 사원 Service
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): 인터페이스 생성
============================================ */
package com.eflix.hr.service;

import java.util.List;

import com.eflix.hr.dto.EmployeesDTO;

public interface EmployeesService {
    public List<EmployeesDTO> getAllEmployees();
    public EmployeesDTO getEmployeeById(String empIdx);
    int createEmployee(EmployeesDTO dto);
    int updateEmployee(EmployeesDTO dto);
    int deleteEmployee(String empIdx);
}
