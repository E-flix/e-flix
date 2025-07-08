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
import java.util.Map;

import com.eflix.hr.dto.SalaryDTO;
import com.eflix.hr.dto.etc.SalaryDetailDTO;
import com.eflix.hr.dto.etc.SalaryFullDetailDTO;
import com.eflix.hr.dto.etc.SalarySummaryDTO;

public interface SalaryService {
    public List<SalaryDTO> getAllSalaries();
    public SalaryDTO getSalaryById(String salaryIdx);
    int createSalary(SalaryDTO dto);
    int updateSalary(SalaryDTO dto);
    int deleteSalary(String salaryIdx);
    
    public List<SalaryDTO> bankList();

    // 0707
    public List<SalarySummaryDTO> findSalaryList(String coIdx, String salaryMonth, String payMonth, String empName,
            String deptIdx);
    public List<SalaryDetailDTO> selectSalaryDetail(String coIdx, String salaryIdx);

    public List<SalaryDetailDTO> findSalaryDetail(String coIdx, String salaryIdx);
    public List<SalaryFullDetailDTO> getSalaryDetailItems(String coIdx, String salaryIdx);
    public void calculateSalary(Map<String, Object> map);
    public void confirmSalary(Map<String, Object> map);
}
