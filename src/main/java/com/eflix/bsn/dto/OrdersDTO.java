package com.eflix.bsn.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

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
}
