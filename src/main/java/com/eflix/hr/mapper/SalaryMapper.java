/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 급여 매핑관리 CRUD용 Mybatis Mapper
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): Mapper 생성
============================================ */
package com.eflix.hr.mapper;

import java.util.List;
import java.util.Map;

import org.springframework.data.repository.query.Param;

import com.eflix.hr.dto.SalaryDTO;
import com.eflix.hr.dto.etc.SalaryDetailDTO;
import com.eflix.hr.dto.etc.SalaryFullDetailDTO;
import com.eflix.hr.dto.etc.SalarySummaryDTO;

public interface SalaryMapper {
    List<SalaryDTO> selectAll();
    SalaryDTO selectById(@Param("salaryIdx") String salaryIdx);
    int insert(SalaryDTO dto);
    int update(SalaryDTO dto);
    int deleteById(@Param("salaryIdx") String salaryIdx);
    
    List<SalaryDTO> bankList();

    List<SalarySummaryDTO> findSalaryList(String coIdx, String salaryMonth, String payMonth, String empName, String deptIdx);

    List<SalaryDetailDTO> findSalaryDetail(String coIdx, String salaryIdx);
    List<SalaryFullDetailDTO> getSalaryDetailItems(String coIdx, String salaryIdx);
    void calculateSalary(Map<String,Object> map);
    void confirmSalary(Map<String,Object> map);
    List<SalaryDetailDTO> selectSalaryDetail(String coIdx, String salaryIdx);
}
