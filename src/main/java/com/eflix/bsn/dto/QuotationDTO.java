package com.eflix.bsn.dto;

import lombok.Data;
import java.time.LocalDate;
import java.util.List;

@Data
public class QuotationDTO {
    private String               quotationNo;       // PK
    private String               customerCd;
    private String               customerName;
    private LocalDate            quotationDt;
    private LocalDate            validPeriod;
    private String               empCd;             // 담당자 코드
    private String               phone;             // 전화번호
    private LocalDate            expectedDeliveryDt;// 납기일
    private String               paymentTerms;      // 결제조건
    private String               attachmentPath;    // 첨부파일
    private List<QuotationDetailDTO> details;       // 상세 리스트
}