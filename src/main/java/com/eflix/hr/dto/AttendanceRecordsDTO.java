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
    private String attdIdx;          // attd_idx
    private String empIdx;           // emp_idx
    private String hdIdx;            // hd_idx
    private Date attdDate;      // attd_date
    private Date attdTime;  // attd_time
    private Date attdStart; // attd_start
    private Date attdEnd;   // attd_end
    private String attdStatus;       // attd_status
    private String abReason;         // ab_reason
    private String memo;             // memo
}
