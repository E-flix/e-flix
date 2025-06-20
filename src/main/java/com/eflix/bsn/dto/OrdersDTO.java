package com.eflix.bsn.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 주문서 DTO
 */
@Getter
@Setter
public class OrdersDTO {
    private String orderNo;
    private String quotationNo;
    private String orderDt;
    private String empCd;
    private String phoneNumber;
    private String deliveryAddr;
    private String warehouseCd;
    private String deliveryDt;
    private String expectedPaymentDt;
    private String paymentTerms;
    private String taxInvoiceIssueYn;
    private String customerCd;
}
