package com.eflix.hr.dto.etc;

import lombok.Data;

@Data
public class SalarySummaryResultDTO {
    private int totalCount;
    private double totalBaseSalary;
    private double totalAllowance;
    private double totalDeduction;
    private double totalNetSalary;
}
