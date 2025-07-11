package com.eflix.main.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.eflix.common.payment.dto.SubscriptionPaymentDTO;
import com.eflix.main.dto.ModuleDTO;
import com.eflix.main.dto.SubscriptionBillDTO;
import com.eflix.main.dto.SubscriptionDTO;
import com.eflix.main.dto.SubscriptionPackageDTO;
import com.eflix.main.dto.SubscriptionPackageDetailDTO;
import com.eflix.main.dto.etc.InvoiceDTO;
import com.eflix.main.dto.etc.StatementDTO;
import com.eflix.main.dto.etc.SubscriptionInfoDTO;
import com.eflix.main.dto.etc.SubscriptionProcedureDTO;

/**
 * 구독 관리 매퍼 인터페이스
 * <p>
 * 이 인터페이스는 MyBatis를 사용하여 구독 관련 데이터베이스 작업을 처리하는
 * 매퍼 메서드들을 정의합니다.
 * <br><br>
 * 구독 등록, 조회, 수정, 삭제 등의 CRUD 작업과 구독 상태 관리,
 * 구독 기간 연장, 구독 통계 조회 등의 비즈니스 로직을 지원합니다.
 * </p>
 *
 * <h3>주요 기능</h3>
 * <ul>
 *   <li><b>구독 등록</b> : 새로운 구독 정보를 데이터베이스에 저장</li>
 *   <li><b>구독 조회</b> : 사용자별, 상품별 구독 정보 조회</li>
 *   <li><b>구독 상태 관리</b> : 구독 활성화, 비활성화, 만료 처리</li>
 *   <li><b>구독 기간 관리</b> : 구독 시작일, 종료일, 갱신일 관리</li>
 *   <li><b>구독 통계</b> : 구독자 수, 매출 통계 등 집계 데이터 조회</li>
 * </ul>
 *
 * @author 복성민 (bokseongmin@gmail.com)
 * @version 1.0
 * @since 2025-06-19
 *
 * @see com.eflix.erp.domain.Subscribe
 * @see com.eflix.erp.service.SubscribeService
 *
 * @changelog
 * <ul>
 *   <li>2025-06-19: 최초 생성 (복성민)</li>
 *   <li>2025-06-23: 구독 상세 값 등록 로직 추가 (복성민)</li>
 *   <li>2025-06-24: 구독 정보 로직 수정 (복성민)</li>
 *   <li>0703</li>
 * </ul>
 */

public interface SubscriptionMapper {
    public SubscriptionPackageDTO findById(String spkIdx);

    public int insertSubscription(SubscriptionDTO subscriptionDTO);

    public int insertSubscriptionPackageDetail(SubscriptionPackageDetailDTO subscriptionPackageDetailDTO);

    public SubscriptionInfoDTO findSubscriptionByCoIdx(String coIdx);

    public List<SubscriptionInfoDTO> findAllSubscriptionByCoIdx(String coIdx);

    public List<SubscriptionBillDTO> findAllSubscriptionBillByCoIdx(String spiIdx);

    public int findActiveSubscriptionByCoIdx(String coIdx);

    public SubscriptionDTO findSubscriptionDetail(String spiIdx);

    public StatementDTO findSubscriptionBySpiIdx(String spiIdx);

    public InvoiceDTO findSubscriptionInvoiceBySpiIdx(String spiIdx);

    // 0703
    public void callInsertSubscription(@Param("dto") SubscriptionProcedureDTO dto);

    public List<ModuleDTO> findAllModuleBySpiIdx(String spiIdx);

    // 0704
    public List<SubscriptionPaymentDTO> findAllByStatus(String string);
}