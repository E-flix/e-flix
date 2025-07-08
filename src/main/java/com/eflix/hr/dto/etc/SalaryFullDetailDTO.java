package com.eflix.hr.dto.etc;

import lombok.Data;

@Data
public class SalaryFullDetailDTO {
    private String salaryIdx;
    private String empIdx;
    private String salaryMonth;
    private String payMonth;
    private double baseSalary;
    private double bonus;
    private double overtimePay;
    private double nightWorkPay;
    private double sal1;
    private double sal2;
    private double sal3;
    private double totalSalary;
    private double tax;
    private double healthInsurance;
    private double nationalPension;
    private double employmentIns;
    private double industrialIns;
    private double otherDeductions;
    private String salaryType;
    private String coIdx;

    private String empName;
    private String deptName;
    private String grdName;
}
