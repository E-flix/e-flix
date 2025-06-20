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

import com.eflix.hr.dto.AttendanceRecordsDTO;

public interface AttendanceRecordsService {
    public List<AttendanceRecordsDTO> getAllAttendanceRecords();
    public AttendanceRecordsDTO getAttendanceRecordById(String attdIdx);
    int createAttendanceRecord(AttendanceRecordsDTO dto);
    int updateAttendanceRecord(AttendanceRecordsDTO dto);
    int deleteAttendanceRecord(String attdIdx);
}
