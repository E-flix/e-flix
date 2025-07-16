package com.eflix.hr.dto.etc;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import lombok.Data;

@Data
public class SalaryEmpDTO {
    private String empIdx;
    private String empName;
    private String empImg;

    private String dpetIdx;
    private String deptName;

    private String grdIdx;
    private String grdName;

    private String attMonth;
    private String payMonth;

    private String salaryType;
}
