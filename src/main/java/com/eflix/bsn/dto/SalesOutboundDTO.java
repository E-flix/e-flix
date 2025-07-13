// SalesOutboundDTO.java
package com.eflix.bsn.dto;

import lombok.Data;
import java.util.Date;
import java.util.List;
import java.util.ArrayList;

/**
 * 출하 의뢰서 헤더 DTO (최신 스키마 및 프론트엔드 요구사항 반영)
 */
@Data
public class SalesOutboundDTO {
    private String outboundNo;
    private String writeDt;
    private String customerCd;
    private String customerName;
    private String representativeNm;
    private Date orderDt;
    private Date outboundDt;
    private Date deliveryDueDate;
    private String money;
    private String remarks;
    private String coIdx;

    /**
     * ★★★ 추가된 필드 ★★★
     * 상세 품목들의 출고 상태를 종합한 전체 상태
     * (예: 출고완료, 일부출고, 대기, 취소)
     */
    private String overallStatus;

    /**
     * 출하 의뢰서 상세 목록
     */
    private List<SoutboundDetailDTO> details = new ArrayList<>();
}
