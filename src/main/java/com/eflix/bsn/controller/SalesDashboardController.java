package com.eflix.bsn.controller;


import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.eflix.bsn.service.SalesDashboardService;
import com.eflix.common.security.auth.AuthUtil;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 영업 대시보드 API Controller
 * - 실시간 매출 현황
 * - 영업 성과 분석
 * - 고객/상품별 인사이트
 */
@Slf4j
@RestController
@RequestMapping("/bsn/dashboard")
@RequiredArgsConstructor
public class SalesDashboardController {

    private final SalesDashboardService dashboardService;

    /**
     * 📊 영업 대시보드 메인 데이터
     */
    @GetMapping("/overview")
    public Map<String, Object> getDashboardOverview() {
        String coIdx = AuthUtil.getCoIdx();
        log.info("영업 대시보드 조회 - 회사: {}", coIdx);

        Map<String, Object> result = new HashMap<>();
        
        try {
            // 오늘 매출
            result.put("todaySales", dashboardService.getTodaySales());
            
            // 이번달 매출
            result.put("monthSales", dashboardService.getMonthSales());
            
            // 견적 → 수주 전환율
            result.put("conversionRate", dashboardService.getQuotationConversionRate());
            
            // 대기중인 주문 건수
            result.put("pendingOrders", dashboardService.getPendingOrdersCount());
            
            // 상위 5개 거래처
            result.put("topCustomers", dashboardService.getTopCustomers(5));
            
            // 최근 7일 매출 트렌드
            result.put("salesTrend", dashboardService.getDailySalesTrend(7));
            
            result.put("success", true);
            
        } catch (Exception e) {
            log.error("영업 대시보드 조회 실패 - 회사: {}", coIdx, e);
            result.put("success", false);
            result.put("message", "대시보드 데이터 조회 중 오류가 발생했습니다.");
        }
        
        return result;
    }

    /**
     * 📈 월별 매출 현황 (차트용)
     */
    @GetMapping("/monthly-sales")
    public Map<String, Object> getMonthlySales(@RequestParam(defaultValue = "12") int months) {
        String coIdx = AuthUtil.getCoIdx();
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            result.put("chartData", dashboardService.getMonthlySalesChart(months));
            result.put("success", true);
            
        } catch (Exception e) {
            log.error("월별 매출 조회 실패 - 회사: {}", coIdx, e);
            result.put("success", false);
            result.put("message", "월별 매출 데이터 조회 실패");
        }
        
        return result;
    }

    /**
     * 🎯 영업 파이프라인 현황
     */
    @GetMapping("/pipeline")
    public Map<String, Object> getSalesPipeline() {
        String coIdx = AuthUtil.getCoIdx();
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            result.put("quotationStage", dashboardService.getQuotationCount());
            result.put("orderStage", dashboardService.getOrderCount());
            result.put("outboundStage", dashboardService.getOutboundCount());
            result.put("conversionRate", dashboardService.getQuotationConversionRate());
            result.put("averageOrderValue", dashboardService.getAverageOrderValue());
            result.put("success", true);
            
        } catch (Exception e) {
            log.error("영업 파이프라인 조회 실패 - 회사: {}", coIdx, e);
            result.put("success", false);
            result.put("message", "파이프라인 데이터 조회 실패");
        }
        
        return result;
    }

    /**
     * 👥 고객 분석 데이터
     */
    @GetMapping("/customer-analytics")
    public Map<String, Object> getCustomerAnalytics() {
        String coIdx = AuthUtil.getCoIdx();
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 고객별 매출 순위 (TOP 10)
            result.put("topCustomersByRevenue", dashboardService.getTopCustomers(10));
            
            // 고객별 주문 빈도 분석
            result.put("customerOrderFrequency", dashboardService.getCustomerOrderFrequency());
            
            // 신규 고객 vs 기존 고객 매출 비율
            result.put("newVsExistingCustomers", dashboardService.getNewVsExistingCustomerRatio());
            
            // 고객 여신 현황 요약
            result.put("creditSummary", dashboardService.getCustomerCreditSummary());
            
            result.put("success", true);
            
        } catch (Exception e) {
            log.error("고객 분석 조회 실패 - 회사: {}", coIdx, e);
            result.put("success", false);
            result.put("message", "고객 분석 데이터 조회 실패");
        }
        
        return result;
    }

    /**
     * 📦 상품 분석 데이터
     */
    @GetMapping("/product-analytics")
    public Map<String, Object> getProductAnalytics() {
        String coIdx = AuthUtil.getCoIdx();
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 베스트셀러 상품 (TOP 10)
            result.put("topProducts", dashboardService.getTopSellingProducts(10));
            
            // 상품별 수익성 분석
            result.put("productProfitability", dashboardService.getProductProfitability());
            
            // 상품 카테고리별 매출
            result.put("categoryRevenue", dashboardService.getCategoryRevenue());
            
            // 재고 회전율 분석
            result.put("inventoryTurnover", dashboardService.getInventoryTurnover());
            
            result.put("success", true);
            
        } catch (Exception e) {
            log.error("상품 분석 조회 실패 - 회사: {}", coIdx, e);
            result.put("success", false);
            result.put("message", "상품 분석 데이터 조회 실패");
        }
        
        return result;
    }

    /**
     * 🏆 영업사원 성과 분석
     */
    @GetMapping("/sales-performance")
    public Map<String, Object> getSalesPerformance() {
        String coIdx = AuthUtil.getCoIdx();
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 영업사원별 매출 실적
            result.put("salesByEmployee", dashboardService.getSalesByEmployee());
            
            // 영업사원별 견적→수주 전환율
            result.put("conversionByEmployee", dashboardService.getConversionRateByEmployee());
            
            // 영업사원별 고객 관리 현황
            result.put("customersByEmployee", dashboardService.getCustomersByEmployee());
            
            // 월별 목표 대비 실적
            result.put("targetVsActual", dashboardService.getTargetVsActualByEmployee());
            
            result.put("success", true);
            
        } catch (Exception e) {
            log.error("영업 성과 조회 실패 - 회사: {}", coIdx, e);
            result.put("success", false);
            result.put("message", "영업 성과 데이터 조회 실패");
        }
        
        return result;
    }

    /**
     * ⚠️ 알림 및 주의사항
     */
    @GetMapping("/alerts")
    public Map<String, Object> getAlerts() {
        String coIdx = AuthUtil.getCoIdx();
        
        Map<String, Object> result = new HashMap<>();
        
        try {
            // 여신 한도 초과 위험 고객
            result.put("creditRiskCustomers", dashboardService.getCreditRiskCustomers());
            
            // 장기 미출고 주문
            result.put("delayedOrders", dashboardService.getDelayedOrders());
            
            // 견적서 유효기간 만료 임박
            result.put("expiringQuotations", dashboardService.getExpiringQuotations());
            
            // 거래정지 고객 목록
            result.put("blockedCustomers", dashboardService.getBlockedCustomers());
            
            result.put("success", true);
            
        } catch (Exception e) {
            log.error("알림 데이터 조회 실패 - 회사: {}", coIdx, e);
            result.put("success", false);
            result.put("message", "알림 데이터 조회 실패");
        }
        
        return result;
    }
}