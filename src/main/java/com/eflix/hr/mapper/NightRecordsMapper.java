/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 야간 기록 CRUD용 Mybatis Mapper
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): Mapper 생성
============================================ */
package com.eflix.hr.mapper;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.eflix.hr.dto.NightRecordsDTO;

public interface NightRecordsMapper {
    List<NightRecordsDTO> selectAll();
    NightRecordsDTO selectById(@Param("ntIdx") String ntIdx);
    int insert(NightRecordsDTO dto);
    int update(NightRecordsDTO dto);
    int deleteById(@Param("ntIdx") String ntIdx);
}
