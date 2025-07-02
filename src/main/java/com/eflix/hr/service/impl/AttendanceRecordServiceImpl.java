/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 근태 기록 ServiceImpl
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): 클래스 생성
============================================ */
package com.eflix.hr.service.impl;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eflix.common.security.auth.AuthContext;
import com.eflix.hr.dto.AttendanceRecordDTO;
import com.eflix.hr.mapper.AttendanceRecordMapper;
import com.eflix.hr.service.AttendanceRecordService;

@Service
public class AttendanceRecordServiceImpl implements AttendanceRecordService {

  @Autowired
  AttendanceRecordMapper attendanceRecordsMapper;
  

    @Autowired
    AuthContext authContext;

  // 근태 전체조회
  @Override
  public List<AttendanceRecordDTO> getAllRecords() {
    return getAllRecords();
  }

  @Override
  public List<AttendanceRecordDTO> getRecordsByEmpId(String empIdx) {
  AttendanceRecordDTO dto = new AttendanceRecordDTO();
    dto.setEmpIdx(empIdx);;
    dto.setCoIdx(authContext.getCoIdx());

    dto.setAttdStart("2025-01-01");
    dto.setAttdEnd(  "2025-06-30");
    return attendanceRecordsMapper.getRecordsByEmpId(dto);
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

}
