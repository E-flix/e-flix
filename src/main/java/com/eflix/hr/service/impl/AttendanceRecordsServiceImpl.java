/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 근태 기록 ServiceImpl
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): 클래스 생성
============================================ */
package com.eflix.hr.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.eflix.hr.dto.AttendanceRecordsDTO;
import com.eflix.hr.service.AttendanceRecordsService;

@Service
public class AttendanceRecordsServiceImpl implements AttendanceRecordsService {

  @Override
  public List<AttendanceRecordsDTO> getAllAttendanceRecords() {

    throw new UnsupportedOperationException("Unimplemented method 'getAllAttendanceRecords'");
  }

  @Override
  public AttendanceRecordsDTO getAttendanceRecordById(String attdIdx) {
    throw new UnsupportedOperationException("Unimplemented method 'getAttendanceRecordById'");
  }

  @Override
  public int createAttendanceRecord(AttendanceRecordsDTO dto) {
    throw new UnsupportedOperationException("Unimplemented method 'createAttendanceRecord'");
  }

  @Override
  public int updateAttendanceRecord(AttendanceRecordsDTO dto) {
    throw new UnsupportedOperationException("Unimplemented method 'updateAttendanceRecord'");
  }

  @Override
  public int deleteAttendanceRecord(String attdIdx) {
    throw new UnsupportedOperationException("Unimplemented method 'deleteAttendanceRecord'");
  }

}
