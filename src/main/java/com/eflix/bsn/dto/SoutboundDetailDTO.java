// SoutboundDetailDTO.java - 새로운 테이블 구조에 맞게 수정
package com.eflix.bsn.dto;

import lombok.Data;
import java.math.BigDecimal;

@Data
public class SoutboundDetailDTO {
    private String outboundNo;      // 출하 의뢰서 번호
    private Integer lineNo;         // 라인 번호 (복수 품목 지원)
    private String itemCode;        // 품목 코드
    private String itemName;        // 품목명 (NOT NULL)
    private String standard;        // 규격 (SPEC → STANDARD로 변경)
    private BigDecimal qty;         // 수량 (NOT NULL)
    private String unit;            // 단위
    private BigDecimal unitPrice;   // 단가
    private BigDecimal supplyAmount; // 공급가액
    private BigDecimal taxAmount;   // 부가세
    private BigDecimal totalAmount; // 합계금액
    private String outboundStatus;  // ★ 새로 추가: 출고 상태 (대기, 준비중, 출고중, 출고완료, 취소)
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
        this.outboundStatus = "대기"; // 기본 출고 상태
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
    
    // ★ 출고 상태 검증 메서드
    public boolean isValidOutboundStatus() {
        if (outboundStatus == null) return false;
        return outboundStatus.equals("대기") || 
               outboundStatus.equals("준비중") || 
               outboundStatus.equals("출고중") || 
               outboundStatus.equals("출고완료") || 
               outboundStatus.equals("취소");
    }
    
    // ★ 출고 상태별 CSS 클래스 반환
    public String getOutboundStatusClass() {
        if (outboundStatus == null) return "bg-secondary";
        
        switch (outboundStatus) {
            case "출고완료": return "bg-success";
            case "출고중": return "bg-primary";
            case "준비중": return "bg-info";
            case "취소": return "bg-danger";
            case "대기":
            default: return "bg-warning";
        }
    }
}