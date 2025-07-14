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
import com.eflix.hr.dto.etc.SalaryCalcDTO;
import com.eflix.hr.dto.etc.SalaryDetailDTO;
import com.eflix.hr.dto.etc.SalaryEmpDTO;
import com.eflix.hr.dto.etc.SalaryFullDetailDTO;
import com.eflix.hr.dto.etc.SalaryListDTO;
import com.eflix.hr.dto.etc.SalarySearchDTO;
import com.eflix.hr.dto.etc.SalarySummaryDTO;

public interface SalaryMapper {
    List<SalaryDTO> selectAll();

    SalaryDTO selectById(@Param("salaryIdx") String salaryIdx);

    int insert(SalaryDTO dto);

    int update(SalaryDTO dto);

    int deleteById(@Param("salaryIdx") String salaryIdx);

    List<SalaryDTO> bankList();

    List<SalarySummaryDTO> findSalaryList(String coIdx, String attMonth, String payMonth, String empName,
            String deptIdx);

    // 0707
    List<SalaryDetailDTO> findSalaryDetail(String coIdx, String salaryIdx);

    List<SalaryFullDetailDTO> getSalaryDetailItems(String coIdx, String salaryIdx);

    void calculateSalary(String coIdx, String salaryIdx);

    void confirmSalary(Map<String, Object> map);

    List<SalaryDetailDTO> selectSalaryDetail(String coIdx, String salaryIdx);

    // 0708
    int insertSalary(SalaryDTO salaryDTO);

    // 0713
    public int findAllCountBySearch(SalarySearchDTO salarySearchDTO);

    public List<SalaryListDTO> findAllBySearch(SalarySearchDTO salarySearchDTO);

    public void calcSalary(SalaryCalcDTO salaryCalcDTO);

    public void calcSalaryByEmpIdx(SalaryCalcDTO salaryCalcDTO);

    public SalaryEmpDTO salaryEmpInfo(String empIdx, String salaryIdx);

    public List<SalaryDetailDTO> salaryDetailBySalaryIdxWithCoIdx(String salaryIdx, String coIdx);

    public int updateSalary(SalaryDTO salaryDTO);
}