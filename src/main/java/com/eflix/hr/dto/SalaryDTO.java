/* ============================================
  - 작성자   : 김어진
  - 최초작성 : 2025-06-19
  - 설명     : 급여 DTO
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
public class SalaryDTO {
    private String salaryIdx;        // salary_idx
    private String empIdx;           // emp_idx
    private String salaryMonth;      // salary_month
    private Double baseSalary;   // base_salary
    private Double bonus;        // bonus
    private Double overtimePay;  // overtime_pay
    private Double nightWorkPay; // night_work_pay
    private Double totalSalary;  // total_salary
    private Double tax;          // tax
    private Date createdDate;   // created_date
    private Double healthInsurance; // health_insurance
    private Double nationalPension; // national_pension
    private Double employmentIns; // employment_ins
    private Double industrialIns; // industrial_ins
    private Double otherDeductions; // other_deductions
    private String payMonth;         // pay_month
    private String attMonth;         // att_month
    private String field;            // field
    private String field2;           // field2
    private String field3;           // field3
    private String field4;           // field4
    private String field5;           // field5
    private String field6;           // field6
    private String field7;           // field7
    private String field8;           // field8
    private String field9;           // field9
    private String field10;          // field10
}
