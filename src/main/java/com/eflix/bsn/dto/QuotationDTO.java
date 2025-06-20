package com.eflix.bsn.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 견적서 DTO
 */
@Getter
@Setter
public class QuotationDTO {
    private String quotationNo;
    private String customerCd;
    private String customerName;
    private String quotationDt;
    private String validPeriod;
    private String empCd;
    private String phone;
    private String deliveryLocation;
    private String expectedDeliveryDt;
    private String paymentTerms;
}
