// SalesOutboundDTO.java
package com.eflix.bsn.dto;

import lombok.Data;
import java.util.Date;
import java.util.List;

@Data
public class SalesOutboundDTO {
    private String outboundNo;          // 출고 번호 (PK)
    private String writeDt;             // 작성일 (문자열)
    private String customerCd;          // 거래처 코드
    private String customerName;
    private String representativeNm;
    private Date orderDt;               // 주문일
    private Date outboundDt;            // 실제 출고일
    private String money;               // 통화
    private String remarks;             // 비고
    private String coIdx;               // 회사 식별자
    private List<SoutboundDetailDTO> details; // 출고 상세 목록
}
