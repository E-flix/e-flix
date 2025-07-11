package com.eflix.bsn.service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * 영업 대시보드 서비스
 * - 실시간 매출 현황 분석
 * - 영업 성과 KPI 제공
 * - 고객/상품별 인사이트
 */
public interface SalesDashboardService {

    // ===== 매출 현황 =====
    
    /**
     * 오늘 매출액
     */
    BigDecimal getTodaySales();
    
    /**
     * 이번달 매출액
     */
    BigDecimal getMonthSales();
    
    /**
     * 월별 매출 차트 데이터 (최근 N개월)
     */
    List<Map<String, Object>> getMonthlySalesChart(int months);
    
    /**
     * 일별 매출 트렌드 (최근 N일)
     */
    List<Map<String, Object>> getDailySalesTrend(int days);

    // ===== 영업 파이프라인 =====
    
    /**
     * 견적서 건수 (이번달)
     */
    int getQuotationCount();
    
    /**
     * 주문서 건수 (이번달)
     */
    int getOrderCount();
    
    /**
     * 출고 건수 (이번달)
     */
    int getOutboundCount();
    
    /**
     * 견적 → 수주 전환율 (%)
     */
    BigDecimal getQuotationConversionRate();
    
    /**
     * 평균 주문 금액
     */
    BigDecimal getAverageOrderValue();
    
    /**
     * 대기중인 주문 건수
     */
    int getPendingOrdersCount();

    // ===== 고객 분석 =====
    
    /**
     * 상위 고객 목록 (매출 기준)
     */
    List<Map<String, Object>> getTopCustomers(int limit);
    
    /**
     * 고객별 주문 빈도 분석
     */
    List<Map<String, Object>> getCustomerOrderFrequency();
    
    /**
     * 신규 vs 기존 고객 매출 비율
     */
    Map<String, Object> getNewVsExistingCustomerRatio();
    
    /**
     * 고객 여신 현황 요약
     */
    Map<String, Object> getCustomerCreditSummary();
    
    /**
     * 여신 위험 고객 목록
     */
    List<Map<String, Object>> getCreditRiskCustomers();

    // ===== 상품 분석 =====
    
    /**
     * 베스트셀러 상품 목록
     */
    List<Map<String, Object>> getTopSellingProducts(int limit);
    
    /**
     * 상품별 수익성 분석
     */
    List<Map<String, Object>> getProductProfitability();
    
    /**
     * 카테고리별 매출
     */
    List<Map<String, Object>> getCategoryRevenue();
    
    /**
     * 재고 회전율 분석
     */
    List<Map<String, Object>> getInventoryTurnover();

    // ===== 영업사원 성과 =====
    
    /**
     * 영업사원별 매출 실적
     */
    List<Map<String, Object>> getSalesByEmployee();
    
    /**
     * 영업사원별 견적→수주 전환율
     */
    List<Map<String, Object>> getConversionRateByEmployee();
    
    /**
     * 영업사원별 고객 관리 현황
     */
    List<Map<String, Object>> getCustomersByEmployee();
    
    /**
     * 영업사원별 목표 대비 실적
     */
    List<Map<String, Object>> getTargetVsActualByEmployee();

    // ===== 알림 및 주의사항 =====
    
    /**
     * 장기 미출고 주문 목록
     */
    List<Map<String, Object>> getDelayedOrders();
    
    /**
     * 견적서 유효기간 만료 임박 목록
     */
    List<Map<String, Object>> getExpiringQuotations();
    
    /**
     * 거래정지 고객 목록
     */
    List<Map<String, Object>> getBlockedCustomers();
}