/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 연차 신청 DTO
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
public class LeaveRequestDTO {
    private String leaveReqIdx;      // leave_req_idx
    private String empIdx;           // emp_idx
    private String approverIdx;      // approver_idx
    private Date leaveStartDate; // leave_start_date
    private Date leaveEndDate;   // leave_end_date
    private String reason;           // reason
    private Date requestDate;   // request_date
    private String approvalStatus;   // approval_status
    private Date approvedDate;  // approved_date
    private String attFile;          // att_file
}