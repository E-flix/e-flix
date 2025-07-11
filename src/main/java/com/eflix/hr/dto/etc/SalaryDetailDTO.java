package com.eflix.hr.dto.etc;

import lombok.Data;

@Data
public class SalaryDetailDTO {
    private String itemName;   // 항목명
    private double amount;     // 금액
    private String remark;     // 비고
    private String taxType;    // 과세/비과세
    private String payType;    // 지급/공제
}
