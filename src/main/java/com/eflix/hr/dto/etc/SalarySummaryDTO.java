package com.eflix.hr.dto.etc;

import lombok.Data;

@Data
public class SalarySummaryDTO {
    private String salaryIdx;
    private String empIdx;
    private String empName;
    private String deptName;
    private String grdName;
    private String attMonth;
    private String payMonth;
    private String baseSalary;
    private int allowanceSum; // -- 지급 합계
    private int deductionSum; // -- 공제 합계
    private String netSalary; // -- 실수령액
    private String status;
}
