/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 근태 기록 Service
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): 인터페이스 생성
============================================ */
package com.eflix.hr.service;

import java.time.LocalDate;
import java.util.List;

import com.eflix.hr.dto.AttendanceRecordDTO;
import com.eflix.hr.dto.EmployeeDTO;

public interface AttendanceRecordService {
    // 근태 전체조회
    public List<AttendanceRecordDTO> getAllRecords();
    
    // 근태 상세조회
    public List<AttendanceRecordDTO> getRecordsByEmpId(AttendanceRecordDTO attendanceRecordDTO);

    // 로그인 사원 근태현황 년월 드롭다운
    public List<LocalDate> getYearMonthList(String empIdx);

    // 로그인 사원 근태현황 기본항목
    public List<AttendanceRecordDTO> getBasicInfo(AttendanceRecordDTO attendanceRecordDTO);

    // 사용자 정보
    public List<AttendanceRecordDTO> userInfo();



    int createAttendanceRecord(AttendanceRecordDTO dto);
    int updateAttendanceRecord(AttendanceRecordDTO dto);
    int deleteAttendanceRecord(String attdIdx);
}
