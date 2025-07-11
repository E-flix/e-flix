package com.eflix.main.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.eflix.common.res.ResUtil;
import com.eflix.common.res.result.ResResult;
import com.eflix.common.res.result.ResStatus;
import com.eflix.main.dto.QuestionDTO;
import com.eflix.main.dto.SubscriptionDTO;
import com.eflix.main.dto.etc.DashboardDTO;
import com.eflix.main.dto.etc.InquirySummaryDTO;
import com.eflix.main.service.DashboardService;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/admin/api")
public class AdminDashboardController {
    
    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/summary")
    public ResponseEntity<ResResult> getDashboardSummary() {
        ResResult result = null;

        DashboardDTO dashboardDTO = dashboardService.getSummary();

        if(dashboardDTO != null) {
            result = ResUtil.makeResult(ResStatus.OK, dashboardDTO);
        } else {
            result = ResUtil.makeResult("404", "데이터를 찾을 수 없습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // @GetMapping("/alerts/subscription")
    // public ResponseEntity<ResResult> getNewSubscriptions() {
    //     ResResult result = null;

    //     List<SubscriptionDTO> subscriptionDTOs = dashboardService.getTodaySubscriptions();

    //     if(subscriptionDTOs != null) {
    //         result = ResUtil.makeResult(ResStatus.OK, subscriptionDTOs);
    //     } else {
    //         result = ResUtil.makeResult("404", "데이터를 찾을 수 없습니다.", null);
    //     }

    //     return new ResponseEntity<>(result, HttpStatus.OK);
    // }

    @GetMapping("/alerts/answers")
    public ResponseEntity<ResResult> getTodayAnswers() {
        ResResult result = null;

        List<InquirySummaryDTO> inquirySummaryDTOs = dashboardService.getTodayAnswers();

        if(inquirySummaryDTOs != null) {
            result = ResUtil.makeResult(ResStatus.OK, inquirySummaryDTOs);
        } else {
            result = ResUtil.makeResult("404", "데이터를 찾을 수 없습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    // 0706
    @GetMapping("/alerts/subscription")
    public ResponseEntity<ResResult> getNewSubscriptions() {
        ResResult result = null;

        List<SubscriptionDTO> subscriptionDTOs = dashboardService.findAllSubscriptions();

        if(subscriptionDTOs != null) {
            result = ResUtil.makeResult(ResStatus.OK, subscriptionDTOs);
        } else {
            result = ResUtil.makeResult("404", "데이터를 찾을 수 없습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/alerts/questions")
    public ResponseEntity<ResResult> getQuestions() {
        ResResult result = null;

        List<QuestionDTO> questionDTOs = dashboardService.findAllQuestions();

        if(questionDTOs != null) {
            result = ResUtil.makeResult(ResStatus.OK, questionDTOs);
        } else {
            result = ResUtil.makeResult("404", "데이터를 찾을 수 없습니다.", null);
        }

        return new ResponseEntity<>(result, HttpStatus.OK);
    }
    
}
