/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 근태 기록 CRUD용 Mybatis Mapper
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): Mapper 생성
============================================ */
package com.eflix.hr.mapper;

import java.util.List;

import org.springframework.data.repository.query.Param;

import com.eflix.hr.dto.AttendanceRecordsDTO;

public interface AttendanceRecordsMapper {
    List<AttendanceRecordsDTO> selectAll();
    AttendanceRecordsDTO selectById(@Param("attdIdx") String attdIdx);
    int insert(AttendanceRecordsDTO dto);
    int update(AttendanceRecordsDTO dto);
    int deleteById(@Param("attdIdx") String attdIdx);
}
