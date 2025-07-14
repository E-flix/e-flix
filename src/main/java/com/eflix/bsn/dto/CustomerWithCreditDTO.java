package com.eflix.bsn.dto;

import lombok.Data;

/**
 * 거래처 정보와 여신 정보를 함께 다루기 위한 DTO
 */
@Data
public class CustomerWithCreditDTO {
    
    private CustomerDTO customer;
    private CreditInfoDTO creditInfo;

}
