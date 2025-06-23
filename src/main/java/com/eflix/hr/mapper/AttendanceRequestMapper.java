/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 근태 요청 CRUD용 Mybatis Mapper
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): Mapper 생성
============================================ */
package com.eflix.hr.mapper;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.eflix.hr.dto.AttendanceRequestDTO;

public interface AttendanceRequestMapper {
    List<AttendanceRequestDTO> selectAll();
    AttendanceRequestDTO selectById(@Param("editIdx") String editIdx);
    int insert(AttendanceRequestDTO dto);
    int update(AttendanceRequestDTO dto);
    int deleteById(@Param("editIdx") String editIdx);
}
