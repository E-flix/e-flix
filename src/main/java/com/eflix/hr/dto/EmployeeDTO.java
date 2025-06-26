/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 사원 DTO
  -----------------------------------------------
  [ 변경 이력 ]
  - 2025-06-19 (김어진): 클래스 생성
  - 2025-06-19 (김어진): 조건별 조회기능 구현
  - 2025-06-24 (김어진): 사원등록 기능 구현
  - 2025-06-26 (김어진): 사원수정 기능 구현
============================================ */
package com.eflix.hr.dto;

import java.sql.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class EmployeeDTO {
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
    private String empPw;           // emp_pw
    private String coIdx;          // co_idx
    private String rrnPfx;
    private String rrnSfx;
    private String empPhone;
    private String empMemo;
    private String empAddr;
    private String empAddrDetail;
    private String empAddrZip;
    
    private String option;
    private String keyword;
}
