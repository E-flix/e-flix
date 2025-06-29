package com.eflix.bsn.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class QuotationDetailDTO {
    private String  quotationNo;    // FK
    private int     lineNo;         // 순번
    private String  itemCode;       // 품목코드
    private String  itemName;
    private String  spec;           // 규격
    private int     qty;
    private BigDecimal unitPrice;
    private BigDecimal totalMoney;
    private BigDecimal taxAmount;   // 부가세
    private String  remarks;        // 적요
}
