// SoutboundDetailDTO.java
package com.eflix.bsn.dto;

import lombok.Data;
import java.math.BigDecimal;

/**
 * =======================================================
 * 출하 의뢰서 상세 (Detail) DTO
 * =======================================================
 * 출하 의뢰서에 포함되는 개별 품목 하나의 정보를 담는 객체입니다.
 */
@Data
public class SoutboundDetailDTO {
    
    /** 출하 의뢰서 번호 (FK) */
    private String outboundNo;
    
    /** 라인 번호 (순번) */
    private Integer lineNo;
    
    /** 품목 코드 */
    private String itemCode;
    
    /** 품목명 (NOT NULL) */
    private String itemName;
    
    /** 규격 (STANDARD 컬럼) */
    private String standard;
    
    /** 수량 (NOT NULL) */
    private BigDecimal qty;
    
    /** 단위 */
    private String unit;
    
    /** 단가 */
    private BigDecimal unitPrice;
    
    /** 공급가액 */
    private BigDecimal supplyAmount;
    
    /** 부가세 */
    private BigDecimal taxAmount;
    
    /** 합계금액 */
    private BigDecimal totalAmount;
    
    /** 출고 상태 (기본값 '대기') */
    private String outboundStatus;
    
    /** 비고 */
    private String remarks;
    
    /** 회사 식별자 */
    private String coIdx;

    /**
     * 금액 자동 계산 로직
     * 수량 또는 단가가 변경될 때 호출하여 공급가액, 부가세, 합계를 재계산합니다.
     */
    public void calculateAmounts() {
        if (this.qty != null && this.unitPrice != null) {
            // 공급가액 계산
            this.supplyAmount = this.qty.multiply(this.unitPrice);
            
            // 부가세 10% 계산 (소수점 첫째 자리에서 반올림)
            if (this.supplyAmount != null) {
                BigDecimal vatRate = new BigDecimal("0.1");
                this.taxAmount = this.supplyAmount.multiply(vatRate).setScale(0, java.math.RoundingMode.HALF_UP);
                this.totalAmount = this.supplyAmount.add(this.taxAmount);
            }
        }
    }
}