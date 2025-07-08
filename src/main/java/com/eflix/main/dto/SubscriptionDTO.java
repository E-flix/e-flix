package com.eflix.main.dto;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

import groovy.transform.builder.Builder;
import lombok.Data;

/**
 * ERP 구독 정보를 전송하기 위한 DTO 클래스
 * 
 * @author 복성민 (bokseongmin@gmail.com)
 * @version 1.0
 * @since 2025-06-19
 * 
 * @see
 * 
 * @changelog
 * <ul>
 *   <li>2025-06-19 복성민: 최초 생성</li>
 *   <li>2025-07-05 복성민: spiNext(구독 결제일) 추가</li>
 * </ul>
 */

@Data
@Builder
public class SubscriptionDTO {
    private String spiIdx;      // spi-000
    private String coIdx;       // co-000
    private String spkIdx;      // spk-000
    private String empIdx;      // emp-000
    private String spiPay;      // SP01: 무통장, SP02: 카드, SP03: 계좌, SP04: 카카오, SP05: 네이버
    private String spiStatus;   // 구독 상태
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date spiStart;      // 구독 시작일
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Seoul")
    private Date spiEnd;        // 구독 종료일
    private int spiPeriod;      // 구독 기간
    private int spiCycle;    // 결제 주기
    private Date spiNext;       // 다음 결제일
    private String spiCtrt;     // 계약서 파일
    private String spiUid;      // PortOne UID(빌링키)
    private String spiMid;      // 거래 고유 ID

    private String spkName;
    private String spkPrice;

    private String coName;
    private String ceoName;
    private String pschName;
    private String pschTel;
    private String pschEmail;

    private List<String> checkedModules;
}
