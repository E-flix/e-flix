package com.eflix.common;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.eflix.common.payment.service.PaymentService;
import com.eflix.hr.service.AttendanceRecordService;
import com.eflix.main.dto.SubscriptionDTO;
import com.eflix.main.service.SubscriptionService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SchedulerConfig {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private SubscriptionService subscriptionService;

    @Autowired
    private AttendanceRecordService attendanceRecordService;
    
    // 정기 결제 처리
    @Scheduled(cron = "0 0 9 * * *")
    public void processSubscriptionPayment() {
        log.info("09:00 구독 결제 처리 중...");
    }

    // 사원 근태 등록
    @Scheduled(cron = "0 0 9 * * *")
    public void insertAttd() {
        attendanceRecordService.addAttd();
        log.info("09:00 사원 근태 정보 등록 중...");
    }
}