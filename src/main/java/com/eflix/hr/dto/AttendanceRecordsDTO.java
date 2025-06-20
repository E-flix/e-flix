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
public class AttendanceRecordsDTO {
    private String empIdx;           // emp_idx
    private String deptIdx;          // dept_idx
    private String empName;          // emp_name
    private String empEmail;         // emp_email
    private Date empRegdate;         // emp_regdate
    private String empGrade;         // emp_grade
    private String empType;          // emp_type
    private String empStatus;        // emp_status
    private String deptName;         // deptName
    private String empAccount;      // emp_account
    
}
