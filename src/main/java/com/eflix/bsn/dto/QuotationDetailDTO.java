package com.eflix.bsn.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class QuotationDetailDTO {
    private String  quotationNo;    // FK
    private Integer     lineNo;         // 순번
    private String  itemCode;       // 품목코드
    private String  itemName;
    private String  spec;           // 규격
    private BigDecimal       qty;
    private BigDecimal unitPrice;
    private BigDecimal supplyAmount;
    private BigDecimal totalMoney;
    private BigDecimal taxAmount;
    private String  remarks;        // 비고
}
