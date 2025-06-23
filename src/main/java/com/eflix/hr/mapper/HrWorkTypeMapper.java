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

import com.eflix.hr.dto.HrWorkTypeDTO;

public interface HrWorkTypeMapper {
    List<HrWorkTypeDTO> selectAll();
    HrWorkTypeDTO selectById(@Param("workTypeId") String workTypeId);
    int insert(HrWorkTypeDTO dto);
    int update(HrWorkTypeDTO dto);
    int deleteById(@Param("workTypeId") String workTypeId);
}
