package com.eflix.bsn.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;

/**
 * 주문서 상세 DTO
 * ORDERS_DETAIL 컬럼 기준
 */
@Data
public class OrdersDetailDTO {

    /** FK - 주문번호 */
    private String orderNo;

    /** 라인번호(순번) */
    private int lineNo;

    /** 품목 코드 */
    private String itemCode;

    /** 품목명 */
    private String itemName;

    /** 규격 */
    private String spec;

    /** 수량 */
    private BigDecimal qty;

    /** 단가 */
    private BigDecimal unitPrice;

    /** 공급가액 */
    private BigDecimal supplyAmount;

    /** 부가세 */
    private BigDecimal taxAmount;

    /** 총합계 */
    private BigDecimal totalAmount;

    /** 비고 */
    private String remarks;

    /** 회사(조직) 구분 */
    private String coIdx;

    /** 출고일 */
    private LocalDate outboundDt;

    /** 적치일 */
    private LocalDate catchDt;

    /** 출고 상태 */
    private String outState;
}
