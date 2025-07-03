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
import com.eflix.hr.dto.EmployeeDTO;
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
  // 근태 상세조회
  @Override
  public List<AttendanceRecordDTO> getRecordsByEmpId(AttendanceRecordDTO attendanceRecordDTO) {
    attendanceRecordDTO.setEmpIdx(attendanceRecordDTO.getEmpIdx());
    attendanceRecordDTO.setCoIdx(authContext.getCoIdx());
    return attendanceRecordsMapper.getRecordsByEmpId(attendanceRecordDTO);
  }

  // 로그인 사원 근태현황 년월 드롭다운
  @Override
  public List<LocalDate> getYearMonthList(String empIdx) {
    return attendanceRecordsMapper.getJoinDate(empIdx, authContext.getCoIdx());
  }

  @Override
  public List<AttendanceRecordDTO> getBasicInfo(AttendanceRecordDTO attendanceRecordDTO) {
    return attendanceRecordsMapper.getBasicInfo(attendanceRecordDTO);
  }

  @Override
  public List<AttendanceRecordDTO> userInfo(AttendanceRecordDTO attendanceRecordDTO) {
    return attendanceRecordsMapper.getBasicInfo(attendanceRecordDTO);
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
