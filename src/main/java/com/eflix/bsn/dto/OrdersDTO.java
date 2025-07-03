package com.eflix.bsn.dto;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * 주문서 헤더 DTO
 * ORDERS 컬럼 기준
 */
@Data
public class OrdersDTO {

    /** PK */
    private String orderNo;

    /** 주문일자 */
    private LocalDate orderDt;

    /** 결제조건 */
    private String paymentTerms;

    /** 거래처 코드 */
    private String customerCd;

    /** 회사(조직) 구분 */
    private String coIdx;

    /** 주문서 작성자 */
    private String orderWriter;

    /** 비고 */
    private String remarks;

    /** 상세 목록 */
    private List<OrdersDetailDTO> details = new ArrayList<>();

    /* ── 신규 필드 ── */
    private String customerNm;        // 거래처명
    private String representativeNm;  // 대표명
    private String phoneNo;           // 연락처
    private String salesEmpCd;        // 영업담당자
    private BigDecimal discountRate;  // 할인율
}
