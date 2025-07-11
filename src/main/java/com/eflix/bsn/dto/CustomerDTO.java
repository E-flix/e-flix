package com.eflix.bsn.dto;

import java.math.BigDecimal;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CustomerDTO {
    private String customerCd;
    private String customerNm;
    private String representativeNm;
    private String phoneNo;
    private String salesEmpCd;
    private BigDecimal discountRate;
}




