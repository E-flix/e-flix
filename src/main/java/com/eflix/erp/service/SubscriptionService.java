package com.eflix.erp.service;

import com.eflix.erp.dto.SubscriptionDTO;
import com.eflix.erp.dto.SubscriptionPackageDTO;
import com.eflix.erp.dto.SubscriptionPackageDetailDTO;
import com.eflix.erp.dto.etc.SubscriptionInfoDTO;

/**
 * ERP 회사 관리를 위한 Service 클래스
 * 
 * <p>
 * 구독 요금 청구서 발행, 견적서 관리, 결제 처리 등의 비즈니스 로직을 처리합니다.
 * 다양한 결제 수단을 지원하며 청구 내역을 체계적으로 관리합니다.
 * </p>
 * 
 * <h3>주요 기능</h3>
 * <ul>
 *   <li>구독 패키지 조회 및 선택</li>
 *   <li>구독 신청 및 계약서 처리</li>
 *   <li>구독 상태 변경 (신청/활성/만료/해지)</li>
 *   <li>구독 기간 연장 및 갱신</li>
 *   <li>패키지별 모듈 권한 관리</li>
 * </ul>
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

public interface SubscriptionService {
    public SubscriptionPackageDTO findById(String spkIdx);

    public int insertSubscriptionInfo(SubscriptionDTO subscriptionDTO);

    public int insertSubscriptionPackageDetail(SubscriptionPackageDetailDTO subscriptionPackageDetailDTO);

    public SubscriptionInfoDTO test(String coIdx);

    public SubscriptionInfoDTO findSubscriptionByCoIdx(String userIdx);
}