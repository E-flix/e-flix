package com.eflix.bsn.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * 견적서 상세 DTO
 */
@Getter
@Setter
public class QuotationDetailDTO {
    private String quotationNo;
    private String itemName;
    private String qty;
    private String unit;
    private String unitPrice;
    private String account;
    private String totalMoney;
    private String remarks;
    private Integer lineNo;
}
