package com.eflix.bsn.mapper;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 영업 대시보드 MyBatis Mapper 인터페이스
 * - 실제 데이터베이스 스키마 기반 구현
 * - 멀티테넌트 지원 (coIdx 기반)
 */
@Mapper
public interface SalesDashboardMapper {

    // ===== 매출 현황 =====
    
    /**
     * 오늘 매출액 조회 (실제 출고 기준)
     */
    BigDecimal getTodaySales(@Param("coIdx") String coIdx);
    
    /**
     * 이번달 매출액 조회 (실제 출고 기준)
     */
    BigDecimal getMonthSales(@Param("coIdx") String coIdx);
    
    /**
     * 월별 매출 차트 데이터 조회 (최근 N개월)
     */
    List<Map<String, Object>> getMonthlySalesChart(Map<String, Object> params);
    
    /**
     * 일별 매출 트렌드 조회 (최근 N일)
     */
    List<Map<String, Object>> getDailySalesTrend(Map<String, Object> params);

    // ===== 영업 파이프라인 =====
    
    /**
     * 견적서 건수 조회 (이번달)
     */
    Integer getQuotationCount(@Param("coIdx") String coIdx);
    
    /**
     * 주문서 건수 조회 (이번달)
     */
    Integer getOrderCount(@Param("coIdx") String coIdx);
    
    /**
     * 출고 건수 조회 (이번달)
     */
    Integer getOutboundCount(@Param("coIdx") String coIdx);
    
    /**
     * 견적 → 수주 전환율 조회 (%)
     */
    BigDecimal getQuotationConversionRate(@Param("coIdx") String coIdx);
    
    /**
     * 평균 주문 금액 조회
     */
    BigDecimal getAverageOrderValue(@Param("coIdx") String coIdx);
    
    /**
     * 대기중인 주문 건수 조회
     */
    Integer getPendingOrdersCount(@Param("coIdx") String coIdx);

    // ===== 고객 분석 =====
    
    /**
     * 상위 고객 목록 조회 (매출 기준)
     */
    List<Map<String, Object>> getTopCustomers(Map<String, Object> params);
    
    /**
     * 고객별 주문 빈도 분석
     */
    List<Map<String, Object>> getCustomerOrderFrequency(@Param("coIdx") String coIdx);
    
    /**
     * 여신 위험 고객 목록 조회
     */
    List<Map<String, Object>> getCreditRiskCustomers(@Param("coIdx") String coIdx);

    // ===== 상품 분석 =====
    
    /**
     * 베스트셀러 상품 목록 조회
     */
    List<Map<String, Object>> getTopSellingProducts(Map<String, Object> params);

    // ===== 영업사원 성과 =====
    
    /**
     * 영업사원별 매출 실적 조회
     */
    List<Map<String, Object>> getSalesByEmployee(@Param("coIdx") String coIdx);

    // ===== 알림 및 주의사항 =====
    
    /**
     * 장기 미출고 주문 목록 조회
     */
    List<Map<String, Object>> getDelayedOrders(@Param("coIdx") String coIdx);
    
    /**
     * 견적서 유효기간 만료 임박 목록 조회
     */
    List<Map<String, Object>> getExpiringQuotations(@Param("coIdx") String coIdx);
    
    /**
     * 거래정지 고객 목록 조회
     */
    List<Map<String, Object>> getBlockedCustomers(@Param("coIdx") String coIdx);
}