/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 근태 요청용 DTO
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): 클래스 생성
============================================ */
package com.eflix.hr.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttendanceRequestDTO {
    private String editIdx;          // edit_idx
    private String attdIdx;          // attd_idx
    private String empIdx;           // emp_idx
    private String approver_idx;
    private String editType;         // edit_type
    private Date oldValue;  // old_value
    private Date newValue;  // new_value
    private String reason;           // reason
    private Date requestDate;   // request_date
    private String approvalStatus;   // approval_status
    private Date approvedDate;  // approved_date
    private String reqFile;

    private String coIdx;

}
