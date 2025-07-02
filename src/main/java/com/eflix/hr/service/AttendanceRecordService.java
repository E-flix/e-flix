/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 근태 기록 Service
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): 인터페이스 생성
============================================ */
package com.eflix.hr.service;

import java.util.List;

import com.eflix.hr.dto.AttendanceRecordDTO;

public interface AttendanceRecordService {
    // 근태 전체조회
    public List<AttendanceRecordDTO> getAllRecords();
    
    // 근태 상세조회
    public List<AttendanceRecordDTO> getRecordsByEmpId(String string);
    
    int createAttendanceRecord(AttendanceRecordDTO dto);
    int updateAttendanceRecord(AttendanceRecordDTO dto);
    int deleteAttendanceRecord(String attdIdx);

}
