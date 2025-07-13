package com.eflix.bsn.dto;

import lombok.Data;

import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

@Data
public class QuotationDTO {
    private String               quotationNo;       // PK
    private String               customerCd;
    private String               customerName;
    // 견적 일시
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date            quotationDt;
    // 유효기간
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Date            validPeriod;
    private String               phone;             // 전화번호
    /** 납기예정일 */
    @DateTimeFormat(pattern = "yyyy-MM-dd") 
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "Asia/Seoul")
    private Date            expectedDeliveryDt;// 납기일
    private String               attachmentPath;    // 첨부파일
    /** 대표명 */
    private String representativeNm;
    /** 발송자 (SENDER 컬럼) */
    private String sender;
    /** 할인율 (0~20, 추가) */
    private Integer discountRate;
    
    /** 견적서 상태 (ACTIVE, ARCHIVED, DELETED) */
    private String status;
    
    /** 아카이브 일시 */
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date archivedAt;

    /** 견적서 상세 목록 */
    private List<QuotationDetailDTO> details;
    
    // 유효성 상태 판단 메소드
    public boolean isExpired() {
        if (validPeriod == null) return false;
        return validPeriod.before(new Date());
    }
    
    public boolean isExpiringSoon() {
        if (validPeriod == null) return false;
        Date today = new Date();
        Date sevenDaysLater = new Date(today.getTime() + (7 * 24 * 60 * 60 * 1000));
        return validPeriod.after(today) && validPeriod.before(sevenDaysLater);
    }
}