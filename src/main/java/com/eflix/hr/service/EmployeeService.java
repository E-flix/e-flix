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

import com.eflix.hr.dto.EmployeeDTO;

public interface EmployeeService {
    public List<EmployeeDTO> getAllEmployees();
    public EmployeeDTO getEmployeeById(String empIdx);
    int createEmployee(EmployeeDTO dto);
    int updateEmployee(EmployeeDTO dto);
    int deleteEmployee(String empIdx);
}
