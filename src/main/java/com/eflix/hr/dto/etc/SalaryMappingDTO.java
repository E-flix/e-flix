package com.eflix.hr.dto.etc;

import lombok.Data;

@Data
public class SalaryMappingDTO {
    private String mpIdx;
    private String alNumber;
    private String salaryType;
    private String alName;
    private String type;
    private String coIdx;
    private double alAmount;
    private String useStatus;
    private String targetDept;
    private String mpMemo;
}
