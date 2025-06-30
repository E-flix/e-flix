package com.eflix.bsn.dto;

import java.math.BigDecimal;

import lombok.Data;

// 주문서 상세 DTO
@Data
public class OrdersDetailDTO {
  private String      orderNo;       // FK
    private Integer     lineNo;        // 순번
    private String      itemCode;      // 품목코드
    private String      itemName;      // 품목명 (조회용)
    private String      spec;          // 규격 (조회용)
    private BigDecimal  qty;           // 수량
    private BigDecimal  unitPrice;     // 단가
    private BigDecimal  supplyAmount;  // 공급가액
    private BigDecimal  taxAmount;     // 부가세
    private BigDecimal  totalAmount;   // 합계
    private String      remarks;       // 비고
}
