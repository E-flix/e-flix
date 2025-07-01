package com.eflix.bsn.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import lombok.Data;

/**
 * 주문서 DTO
 */

@Data
public class OrdersDTO {
    private String  orderNo;
    private String  quotationNo;
    private LocalDate orderDate;
    private String  empCd;        // 영업사원 코드
    private String   phoneNumber;     // ← PHONE_NUMBER 컬럼 매핑
    private String   deliveryAddr;    // DELIVERY_ADDR 컬럼
    private String   warehouseCd;     // WAREHOUSE_CD 컬럼
    private LocalDate deliveryDt;     // DELIVERY_DT 컬럼
    private LocalDate expectedPaymentDt; // EXPECTED_PAYMENT_DT 컬럼
    private String   paymentTerms;
    private String   taxInvoiceIssueYn;
    private String   customerCd;
    private String taxInvoiceYn;   // ← Y / N 값 들어올 곳
    private BigDecimal totalAmount;
    private BigDecimal orderTotal;
    private List<OrdersDetailDTO> details;
}