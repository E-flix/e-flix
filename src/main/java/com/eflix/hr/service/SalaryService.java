/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 급여 Service
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): 인터페이스 생성
============================================ */
package com.eflix.hr.service;

import java.util.List;

import com.eflix.hr.dto.SalaryDTO;

public interface SalaryService {
    public List<SalaryDTO> getAllSalaries();
    public SalaryDTO getSalaryById(String salaryIdx);
    int createSalary(SalaryDTO dto);
    int updateSalary(SalaryDTO dto);
    int deleteSalary(String salaryIdx);
}
