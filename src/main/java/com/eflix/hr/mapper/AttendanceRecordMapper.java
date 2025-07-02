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
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.springframework.data.repository.query.Param;

import com.eflix.hr.dto.AttendanceRecordDTO;

@Mapper
public interface AttendanceRecordMapper {

    // 근태 전체조회
    List<AttendanceRecordDTO> getAllRecords();

    // 근태 상세조회
    List<AttendanceRecordDTO> getRecordsByEmpId(AttendanceRecordDTO dto);
    
    int insert(AttendanceRecordDTO dto);
    int update(AttendanceRecordDTO dto);
    int deleteById(@Param("attdIdx") String attdIdx);


}
