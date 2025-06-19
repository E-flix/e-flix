/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 부서 이력 관리용 DTO
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
public class DepartmentRecordDTO {
    private String deptHistId;       // dept_hist_id
    private String empIdx;           // emp_idx
    private String beforeDeptId;     // before_dept_id
    private String afterDeptId;      // after_dept_id
    private Date changeDate;    // change_date
    private String reason;           // reason
    private Date createdAt; // created_at
    private String createdBy;        // created_by
}