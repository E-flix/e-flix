package com.eflix.mgr.mapper;

import java.util.List;
import java.util.Map;

public interface SummaryMapper {
    // 요약 카드
    public int getOnDutyCount();
    public int getTardyCount();
    
    public int getThisMonthSales();

    public int getInventoryCount();
    public int getStockShortageCount();

    public int getTodayProcessCount();

    // 인사 모듈
    public List<Map<String, Object>> getAttendanceChartData();

    // 회계 모듈
    public List<Map<String, Object>> getSalaryTrend();
    public List<Map<String, Object>> getPendingPayouts();

    // 재고 모듈
    public List<Map<String, Object>> getInventoryFlow();
    public Map<String, Object> getLowStockItems();

    //  영업 모듈
    public List<Map<String, Object>> getSalesTrend();
    public List<Map<String, Object>> getWeeklyOrderStats();

}