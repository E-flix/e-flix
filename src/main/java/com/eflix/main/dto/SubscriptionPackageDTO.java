package com.eflix.main.dto;

import groovy.transform.builder.Builder;
import lombok.Data;

/**
 * ERP 구독 패키지 정보를 전송하기 위한 DTO 클래스
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
public class SubscriptionPackageDTO {
    private String spkIdx;      // spk-000
    private String spkName;
    private Long spkMax;
    private Long spkPrice;
}