// SoutboundDetailDTO.java
package com.eflix.bsn.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SoutboundDetailDTO {
    private String orderNo;    // 출고 번호(FK → SALES_OUTBOUND.OUTBOUND_NO)
    private String itemName;   // 품목명
    private String standard;   // 규격
    private BigDecimal qty;    // 수량
    private String unit;       // 단위
    private String remarks;    // 비고
    private String coIdx;      // 회사 식별자
}
