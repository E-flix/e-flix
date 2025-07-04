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

import org.springframework.data.repository.query.Param;

import com.eflix.hr.dto.SalaryMappingDTO;

public interface SalaryMappingMapper {
    List<SalaryMappingDTO> selectAll();
    SalaryMappingDTO selectById(@Param("mpIdx") String mpIdx);
    int insert(SalaryMappingDTO dto);
    int update(SalaryMappingDTO dto);
    int deleteById(@Param("mpIdx") String mpIdx);
}
