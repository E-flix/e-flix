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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eflix.hr.dto.AttendanceRecordDTO;
import com.eflix.hr.mapper.AttendanceRecordMapper;
import com.eflix.hr.service.AttendanceRecordService;

@Service
public class AttendanceRecordServiceImpl implements AttendanceRecordService {

  @Autowired
  AttendanceRecordMapper attendanceRecordsMapper;

  // @Override
  // public List<AttendanceRecordDTO> getAllAttendanceRecord(String attdIdxx) {
  //   throw new UnsupportedOperationException("Unimplemented method 'getAttendanceRecordById'");
  // }

  @Override
  public AttendanceRecordDTO getAttendanceRecordById(String attdIdx) {
    throw new UnsupportedOperationException("Unimplemented method 'getAttendanceRecordById'");
  }

  @Override
  public int createAttendanceRecord(AttendanceRecordDTO dto) {
    throw new UnsupportedOperationException("Unimplemented method 'createAttendanceRecord'");
  }

  @Override
  public int updateAttendanceRecord(AttendanceRecordDTO dto) {
    throw new UnsupportedOperationException("Unimplemented method 'updateAttendanceRecord'");
  }

  @Override
  public int deleteAttendanceRecord(String attdIdx) {
    throw new UnsupportedOperationException("Unimplemented method 'deleteAttendanceRecord'");
  }

  @Override
  public List<AttendanceRecordDTO> getAllAttendanceRecords() {
    throw new UnsupportedOperationException("Unimplemented method 'getAllAttendanceRecords'");
  }

}
