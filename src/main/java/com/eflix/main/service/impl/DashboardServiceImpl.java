package com.eflix.main.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eflix.main.dto.SubscriptionDTO;
import com.eflix.main.dto.etc.DashboardDTO;
import com.eflix.main.dto.etc.InquirySummaryDTO;
import com.eflix.main.mapper.DashboardMapper;
import com.eflix.main.service.DashboardService;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private DashboardMapper dashboardMapper;

    @Override
    public DashboardDTO getSummary() {
        DashboardDTO dashboardDTO = new DashboardDTO();
        dashboardDTO.setSubscriberCount(dashboardMapper.countActiveSubscribers());
        dashboardDTO.setSubscriptionRequestCount(dashboardMapper.countSubscriptionRequests());
        dashboardDTO.setUnansweredCount(dashboardMapper.countUnansweredQuestions());
        dashboardDTO.setTodayAnsweredCount(dashboardMapper.countTodayAnswers());
        return dashboardDTO;
    }

    @Override
    public List<SubscriptionDTO> getTodaySubscriptions() {
        return dashboardMapper.selectTodaySubscriptions();
    }

    @Override
    public List<InquirySummaryDTO> getTodayAnswers() {
        return dashboardMapper.selectTodayAnswers();
    }
    
}
