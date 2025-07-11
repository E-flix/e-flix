// SalesOutboundDTO.java
package com.eflix.bsn.dto;

import lombok.Data;
import java.util.Date;
import java.util.List;

/**
 * 출하 의뢰서 DTO
 * 출하 의뢰서 헤더 정보를 담는 데이터 전송 객체
 */
@Data
public class SalesOutboundDTO {
    private String outboundNo;          // 출하 의뢰서 번호 (PK)
    private String writeDt;             // 작성일 (문자열)
    private String customerCd;          // 거래처 코드
    private String customerName;        // 거래처명
    private String representativeNm;    // 대표명
    private Date orderDt;               // 주문일
    private Date outboundDt;            // 실제 출하일
    private String money;               // 통화
    private String remarks;             // 비고
    private String coIdx;               // 회사 식별자
    private List<SoutboundDetailDTO> details; // 출하 의뢰서 상세 목록
}