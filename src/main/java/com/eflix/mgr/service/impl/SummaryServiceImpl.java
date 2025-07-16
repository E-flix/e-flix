package com.eflix.mgr.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eflix.mgr.dto.etc.AccDTO;
import com.eflix.mgr.dto.etc.BsnDTO;
import com.eflix.mgr.dto.etc.HrDTO;
import com.eflix.mgr.dto.etc.PurchsDTO;
import com.eflix.mgr.dto.etc.SummaryDTO;
import com.eflix.mgr.mapper.SummaryMapper;
import com.eflix.mgr.service.SummaryService;

@Service
public class SummaryServiceImpl implements SummaryService {
   
    @Autowired
    private SummaryMapper summaryMapper;

    @Override
    public SummaryDTO getSummary() {
        SummaryDTO summaryDTO = new SummaryDTO(
            summaryMapper.getOnDutyCount(), 
            summaryMapper.getTardyCount(),
            // summaryMapper.getThisMonthSales(),
            summaryMapper.getInventoryCount(),
            summaryMapper.getStockShortageCount(),
            summaryMapper.getTodayProcessCount());
        return summaryDTO;
    }

    @Override
    public HrDTO getHr() {
        List<Map<String, Object>> chart = summaryMapper.getAttendanceChartData();
        return new HrDTO(chart);
    }

    @Override
    public AccDTO getAcc() {
        List<Map<String, Object>> trend = summaryMapper.getSalaryTrend();
        List<Map<String, Object>> pending = summaryMapper.getPendingPayouts();
        return new AccDTO(trend, pending);
    }

    @Override
    public PurchsDTO getPurchs() {
        List<Map<String, Object>> flow = summaryMapper.getInventoryFlow();
        return new PurchsDTO(flow);
    }

    @Override
    public BsnDTO getBsn() {
        List<Map<String, Object>> salesTrend = summaryMapper.getSalesTrend();
        List<Map<String, Object>> weekly = summaryMapper.getWeeklyOrderStats();
        return new BsnDTO(salesTrend, weekly);
    }

    
}
