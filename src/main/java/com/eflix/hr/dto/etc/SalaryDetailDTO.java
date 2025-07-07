package com.eflix.hr.dto.etc;

import lombok.Data;

@Data
public class SalaryDetailDTO {
    private String item;
    private int amount;
    private String memo;
    private String taxType;
    private String payType;
}
