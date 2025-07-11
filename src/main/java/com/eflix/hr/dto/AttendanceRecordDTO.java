/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 출퇴근·근태 기록 조회용 DTO
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): 클래스 생성
============================================ */
package com.eflix.hr.dto;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRecordDTO {
    private String attdIdx;
    private String empIdx;       // emp_idx
    private String deptIdx;      // dept_idx
    private String empName;      // emp_name
    private String empEmail;     // emp_email
    private Date   empRegdate;   // emp_regdate
    private String empGrade;     // emp_grade
    private String empType;      // emp_type
    private String empStatus;    // emp_status
    private String deptName;     // dept_name
    private String empAccount;   // emp_account
    private String coIdx;        // co_idx

    // Attendance Detail Fields
    private Date   attdDate;     // attendance_records.attd_date
    private String hdIdx;        // attendance_records.hd_idx
    private String attdStatus;   // attendance_records.attd_status
    private String attdTime;     // formatted attendance_records.attd_time
    private String attdStart;    // formatted attendance_records.attd_start
    private String attdEnd;      // formatted attendance_records.attd_end
    private String overtime;     // calculated overtime
    
    private Date hdDate;
    private String hdType;
    private Double leaveAllDays;
    private Double leaveDays;

}
