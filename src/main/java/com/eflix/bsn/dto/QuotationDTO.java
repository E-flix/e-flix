package com.eflix.bsn.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class QuotationDTO {
    private String               quotationNo;       // PK
    private String               customerCd;
    private String               customerName;
    // 견적 일시
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date            quotationDt;
    // 유효기간
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Date            validPeriod;
    private String               empCd;             // 담당자 코드
    private String               phone;             // 전화번호
    /** 납기예정일 */
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Date            expectedDeliveryDt;// 납기일
    private String               paymentTerms;      // 결제조건
    private String               attachmentPath;    // 첨부파일
    /** 대표명 */
    private String representativeNm;
    /** 할인율 (0~20, 추가) */
    private Integer discountRate;
    private List<QuotationDetailDTO> details;       // 상세 리스트
}

