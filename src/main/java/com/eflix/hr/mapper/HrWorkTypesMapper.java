/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 인사 근무 유형 CRUD용 Mybatis Mapper
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): Mapper 생성
============================================ */
package com.eflix.hr.mapper;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.eflix.hr.dto.HrWorkTypesDTO;

public interface HrWorkTypesMapper {
    List<HrWorkTypesDTO> selectAll();
    HrWorkTypesDTO selectById(@Param("workTypeId") String workTypeId);
    int insert(HrWorkTypesDTO dto);
    int update(HrWorkTypesDTO dto);
    int deleteById(@Param("workTypeId") String workTypeId);
}
