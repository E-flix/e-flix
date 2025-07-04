package com.eflix.common;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.eflix.common.payment.service.PaymentService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class SchedulerConfig {

    @Autowired
    private PaymentService paymentService;
    
    @Scheduled(cron = "0 0 9 * * *")
    public void processSubscriptionPayment() {
        log.info("09:00 구독 결제 처리 중...");

        paymentService.processSubscription();
    }
}
