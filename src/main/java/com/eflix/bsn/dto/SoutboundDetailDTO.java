// SoutboundDetailDTO.java
package com.eflix.bsn.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * 출하 의뢰서 상세 DTO (가격 필드 제거 버전)
 */
@Data
public class SoutboundDetailDTO {
    
    private String outboundNo;      // 출하 의뢰서 번호 (FK)
    private Integer lineNo;         // 라인 번호
    private String itemCode;        // 품목 코드
    private String itemName;        // 품목명 (NOT NULL)
    private String standard;        // 규격
    private BigDecimal qty;         // 수량 (NOT NULL)
    private String unit;            // 단위
    private String outboundStatus;  // 출고 상태
    private String remarks;         // 비고
    private String coIdx;           // 회사 식별자
    
}
