package com.eflix.hr.dto.etc;

import lombok.Data;

@Data
public class SalaryListDTO {
    private String salaryIdx;
    private String empIdx;
    private String empName;
    private String deptName;
    private String grdName;
    private String attMonth;
    private String payMonth;
    private double baseSalary;
    private double allowanceTotal;
    private double deductionTotal;
    private double netSalary;
    private String calcStatus;
    private String salaryType;

}
