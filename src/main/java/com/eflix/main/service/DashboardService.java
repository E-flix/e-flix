package com.eflix.main.service;

import java.util.List;

import com.eflix.main.dto.QuestionDTO;
import com.eflix.main.dto.SubscriptionDTO;
import com.eflix.main.dto.etc.DashboardDTO;
import com.eflix.main.dto.etc.InquirySummaryDTO;

public interface DashboardService {
    public DashboardDTO getSummary();
    public List<SubscriptionDTO> getTodaySubscriptions();
    public List<InquirySummaryDTO> getTodayAnswers();

    // 0706
    public List<SubscriptionDTO> findAllSubscriptions();

    public List<QuestionDTO> findAllQuestions();
}
