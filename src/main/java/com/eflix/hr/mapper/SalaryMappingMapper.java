/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 급여 CRUD용 Mybatis Mapper
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): Mapper 생성
============================================ */
package com.eflix.hr.mapper;

import java.util.List;

import com.eflix.hr.dto.etc.SalaryMappingDTO;
import com.eflix.hr.dto.etc.SalaryMappingSearchDTO;

public interface SalaryMappingMapper {
    public int findAllItemCount(SalaryMappingSearchDTO salaryMappingSearchDTO);

    public List<SalaryMappingDTO> findAllBySearch(SalaryMappingSearchDTO salaryMappingSearchDTO);

    public int insert(SalaryMappingDTO salaryMappingDTO);

    public SalaryMappingDTO findByMpIdx(String mpIdx);

    public int update(SalaryMappingDTO salaryMappingDTO);

    public int delete(String mpIdx);

    List<SalaryMappingDTO> findAllByCoIdx(String coIdx);

    // SalaryMappingDTO findByMpIdx(String coIdx, String mpIdx);

    // int insert(SalaryMappingDTO salaryMappingDTO);

    // int update(SalaryMappingDTO salaryMappingDTO);

    // int delete(String mpIdx);
}
