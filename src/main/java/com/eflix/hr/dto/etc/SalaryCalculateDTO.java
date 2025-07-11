package com.eflix.hr.dto.etc;

import lombok.Data;

@Data
public class SalaryCalculateDTO {
    private String alName;
    private String type;
    private double alAmount;
    private double calculatedAmount;

}