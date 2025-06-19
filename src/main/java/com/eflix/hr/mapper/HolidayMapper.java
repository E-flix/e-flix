/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 휴일 CRUD용 Mybatis Mapper
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): Mapper 생성
============================================ */
package com.eflix.hr.mapper;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.eflix.hr.dto.HolidayDTO;

public interface HolidayMapper {
    List<HolidayDTO> selectAll();
    HolidayDTO selectById(@Param("hdIdx") String hdIdx);
    int insert(HolidayDTO dto);
    int update(HolidayDTO dto);
    int deleteById(@Param("hdIdx") String hdIdx);
}
