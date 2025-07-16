/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 부서 DTO
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
public class DepartmentDTO {
    private String deptIdx; // dept_idx
    private String deptUpIdx; // dept_up_idx
    private String deptName; // dept_name
    private String deptStatus; // dept_status
    private String deptMemo; // dept_memo
    private String deptTel; // dept_memo
    private Date deptRegdate; // dept_regdate
    private Date deptModdate; // dept_moddate
    private String mgrIdx;

    private String deptUpName; // 상위 부서 이름
    private String empCount;

    private String coIdx;

}
