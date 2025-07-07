package com.eflix.acc.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 매입매출장 DTO
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseSalesReportDTO {
    private String type;           // 유형 (과세 등)
    private String date;           // 일자
    private String item;           // 품목
    private Long supplyAmount;     // 공급가액
    private Long tax;              // 세액
    private Long total;            // 합계
    private String code;           // 코드
    private String partnerName;    // 거래처명
    private String accountCode;    // 계정코드
    private String accountName;    // 계정과목명
    private String electronic;     // 전자
    // 필요시 추가 필드
}
