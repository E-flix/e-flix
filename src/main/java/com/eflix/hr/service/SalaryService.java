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
import com.eflix.hr.dto.etc.SalaryCalcDTO;
import com.eflix.hr.dto.etc.SalaryDetailDTO;
import com.eflix.hr.dto.etc.SalaryEmpDTO;
import com.eflix.hr.dto.etc.SalaryFullDetailDTO;
import com.eflix.hr.dto.etc.SalaryListDTO;
import com.eflix.hr.dto.etc.SalarySearchDTO;
import com.eflix.hr.dto.etc.SalarySummaryDTO;

public interface SalaryService {

    public List<SalaryDTO> bankList();

    // 0707
    public List<SalarySummaryDTO> findSalaryList(String coIdx, String attMonth, String payMonth, String empName,
            String deptIdx);

    public List<SalaryDetailDTO> selectSalaryDetail(String coIdx, String salaryIdx);

    public List<SalaryDetailDTO> findSalaryDetail(String coIdx, String salaryIdx);

    public List<SalaryFullDetailDTO> getSalaryDetailItems(String coIdx, String salaryIdx);

    public void calculateSalary(String coIdx, List<String> salaryIdxList);

    public void confirmSalary(Map<String, Object> map);

    // 0708
    public int insertSalary(SalaryDTO salaryDTO);

    // 0713
    public int findAllCountBySearch(SalarySearchDTO salarySearchDTO);

    public List<SalaryListDTO> findAllBySearch(SalarySearchDTO salarySearchDTO);

    public boolean calcSalary(SalaryCalcDTO salaries);

    public int calcSalaryByEmpIdx(SalaryCalcDTO salaryCalcDTO);

    public SalaryEmpDTO salaryEmpInfo(String empIdx, String salaryIdx);

    public List<SalaryDetailDTO> salaryDetailBySalaryIdxWithCoIdx(String salaryIdx, String coIdx);

    public int confirmSalary(List<String> salaryDTO);
}
