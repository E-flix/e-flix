// SoutboundDetailDTO.java
package com.eflix.bsn.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SoutboundDetailDTO {
    private String outboundNo;      // ★ 수정: orderNo → outboundNo (의미 명확화)
    private Integer lineNo;         // ★ 추가: 라인 번호 (복수 품목 지원)
    private String itemCode;        // ★ 추가: 품목 코드
    private String itemName;        // 품목명
    private String standard;        // 규격 (spec → standard)
    private BigDecimal qty;         // 수량
    private String unit;            // 단위
    private BigDecimal unitPrice;   // ★ 추가: 단가
    private BigDecimal supplyAmount; // ★ 추가: 공급가액
    private BigDecimal taxAmount;   // ★ 추가: 부가세
    private BigDecimal totalAmount; // ★ 추가: 합계금액
    private String remarks;         // 비고
    private String coIdx;           // 회사 식별자
    
    // ★ 기본 생성자
    public SoutboundDetailDTO() {}
    
    // ★ 편의 생성자 (필수 필드만)
    public SoutboundDetailDTO(String outboundNo, Integer lineNo, String itemName) {
        this.outboundNo = outboundNo;
        this.lineNo = lineNo;
        this.itemName = itemName;
        this.qty = BigDecimal.ONE;  // 기본값
    }
    
    // ★ 비즈니스 로직: 금액 자동 계산
    public void calculateAmounts() {
        if (qty != null && unitPrice != null) {
            supplyAmount = qty.multiply(unitPrice);
            
            // 부가세 10% 계산
            if (supplyAmount != null) {
                taxAmount = supplyAmount.multiply(new BigDecimal("0.1"));
                totalAmount = supplyAmount.add(taxAmount);
            }
        }
    }
}