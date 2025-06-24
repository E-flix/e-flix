package com.eflix.erp.dto;

import groovy.transform.builder.Builder;
import lombok.Data;

/**
 * ERP 구독 상세 정보를 전송하기 위한 DTO 클래스
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
public class SubscriptionPackageDetailDTO {
    private String spdIdx;      // spd-000
    private String spkIdx;      // spk-000
    private String moduleIdx;   // mou-000
    private String spiIdx;      // spi-000
}