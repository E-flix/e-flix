/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 사원 DTO
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
public class EmployeesDTO {
    private String empIdx;           // emp_idx
    private String deptIdx;          // dept_idx
    private String empImg;           // emp_img
    private String empName;          // emp_name
    private String rrnPfx;           // rrn_pfx
    private String rrnSfx;           // rrn_sfx
    private String empEmail;         // emp_email
    private String empPw;            // emp_pw
    private Date empRegdate;    // emp_regdate
    private String empGrade;         // emp_grade
    private String empRole;          // emp_role
    private String empType;          // emp_type
    private String empAccount;       // emp_account
    private String empAddr;          // emp_addr
    private String empAddrDetail;    // emp_addr_detail
    private Integer empAddrZip;      // emp_addr_zip
    private String empMemo;          // emp_memo
    private String empStatus;        // emp_status
    private String empContract;      // emp_contract
    private String bankCode;         // bank_code
    private Double leaveAllDays; // leave_all_days
    private Double leaveDays;    // leave_days
}
