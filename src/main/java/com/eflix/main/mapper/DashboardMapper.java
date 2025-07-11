package com.eflix.main.mapper;

import java.util.List;

import com.eflix.main.dto.QuestionDTO;
import com.eflix.main.dto.SubscriptionDTO;
import com.eflix.main.dto.etc.InquirySummaryDTO;

public interface DashboardMapper {
    public int countActiveSubscribers();         // spi_status = 'SS01'
    
    public int countSubscriptionRequests();      // 상태 SS02 또는 조건 추가 가능

    public List<SubscriptionDTO> selectTodaySubscriptions();  // 금일 등록된 구독
    
    public int countUnansweredQuestions();       // answers에 존재하지 않는 questions

    public int countTodayAnswers();              // 금일 등록된 답변 수

    public List<InquirySummaryDTO> selectTodayAnswers();              // 금일 등록된 답변

    // 0706
    public List<SubscriptionDTO> findAllSubscriptions(); // 신규 구독자
    
    public List<QuestionDTO> findAllQuestions(); // 미 답변 목록
}
