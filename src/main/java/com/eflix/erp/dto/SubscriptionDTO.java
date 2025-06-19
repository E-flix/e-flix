package com.eflix.erp.dto;

import java.util.Date;

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
 *   <li>2025-06-19: 최초 생성 (복성민)</li>
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
    private String spiStatus;
    private Date spiStart;
    private Date spiEnd;
    private Date spiPeriod;
    private String spiCtrt;     // 계약서 파일
}
